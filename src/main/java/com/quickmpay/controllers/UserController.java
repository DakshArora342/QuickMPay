package com.quickmpay.controllers;

import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.quickmpay.dtos.KycDetailsDto;
import com.quickmpay.dtos.LoginDto;
import com.quickmpay.dtos.RiskyAccountDto;
import com.quickmpay.dtos.RoleDto;
import com.quickmpay.dtos.UserAccountDto;
import com.quickmpay.dtos.UserDto;
import com.quickmpay.entities.Order;
import com.quickmpay.entities.OrderRecord;
import com.quickmpay.entities.Rate;
import com.quickmpay.entities.Tips;
import com.quickmpay.entities.User;
import com.quickmpay.entities.Wallet;
import com.quickmpay.extras.DateAndTimeProvider;
import com.quickmpay.extras.EmailSender;
import com.quickmpay.extras.StringUtil;
import com.quickmpay.services.AccountDetails;
import com.quickmpay.services.AdminService;
import com.quickmpay.services.UserService;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
//@RequestMapping(value = "/user")
public class UserController {
	private List<UserAccountDto> list;

	@Autowired
	UserService userService;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	AccountDetails accountDetails;

	@Autowired
	ModelMapper modelMapper;

	@PostMapping("/register")
	public String register(@Valid @ModelAttribute UserDto userDto, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("userDto", userDto);
			return "signup";
		} else {
			UserDto user = userService.addUser(userDto);
			if (user != null) {
				model.addAttribute("user", user);
				return "verify";
			}
			model.addAttribute("msg", "Email or Mobile already registered");
			return "signup";
		}

	}

	@PostMapping("/login")
	public String login(@Valid @ModelAttribute LoginDto loginDto, BindingResult bindingResult, Model model,
			HttpSession httpSession,@RequestParam(defaultValue = "0")int pageNo) {
		try {
			if (bindingResult.hasErrors()) {
				return "login";
			} else {
				Authentication authentication = authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
				if (authentication.isAuthenticated()) {
					String string = userService.findUser(loginDto);
					if (string.equals("active")) {
						UserDto userDto = userService.findUserByEmail(loginDto.getEmail());
						Set<RoleDto> roles = userDto.getRoles();

						String roleNamesString = roles.stream().map(RoleDto::getName).collect(Collectors.joining(", "));
						httpSession.setAttribute("id", userDto.getId());

						if (roleNamesString.equalsIgnoreCase("ADMIN")) {

							httpSession.setAttribute("name", "Welcome: " + userDto.getName());
//							httpSession.setAttribute("allusers",userService.getAllUsers());
//							httpSession.setAttribute("allusers",userService.getAllUsers(pageNo));
							return "admin";
						}else {
						httpSession.setAttribute("name", "Welcome: " + userDto.getName());
						Tips tipsById = userService.findTipsById(1);
						if(tipsById!=null) {
							httpSession.setAttribute("tips", userService.findTipsById(1));
							return "accountType";
						}else {
							tipsById=new Tips();
							tipsById.setTip("There are currently no tips available.");
							httpSession.setAttribute("tips", tipsById);
							return "accountType";
						}
						}
					} else {
						model.addAttribute("user", loginDto);
						userService.resendOtp(loginDto);
						return "verify";
					}
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			model.addAttribute("msg", "Invalid Email or Password");
			return "login";
		}
		return null;

	}

	@PostMapping("/verify")
	public String verify(@RequestParam String action, @ModelAttribute LoginDto loginDto, Model model) {
		if ("verify".equals(action)) {
			String string = userService.verify(loginDto.getEmail(), loginDto.getOtp());
			if (string != null) {
				return "login";
			} else {
				model.addAttribute("msg", "Invalid OTP");
				model.addAttribute("user", loginDto);
				return "verify";
			}
		} else if ("resend".equals(action)) {
			LoginDto user = userService.resendOtp(loginDto);
			if (user != null) {
				model.addAttribute("msg", "OTP resend successful");
				model.addAttribute("user", user);
				return "verify";
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	@PostMapping("/safeUserAccountDetails")
	public String safeUserAccountDetails(@Valid @ModelAttribute UserAccountDto userAccountDto,
			BindingResult bindingResult, Model model, HttpSession httpSession) {
		if (bindingResult.hasErrors()) {
			return "SafeUserAccountDetails";
		} else {
			UserDto userDto = userService.findUserById((String) httpSession.getAttribute("id"));
			UserAccountDto accounts = userDto.getAccounts();
			if (accounts != null) {
				if (accounts.getAccountNo() == null) {
					accounts.setAccountHolderName(userAccountDto.getAccountHolderName());
					accounts.setAccountNo(userAccountDto.getAccountNo());
					accounts.setBankName(userAccountDto.getBankName());
					accounts.setIfscCode(userAccountDto.getIfscCode());
					userDto.setAccounts(accounts);
					userService.updateUser(userDto);
					model.addAttribute("msg", "Account added successfully.");
					return "safe_user_success_dashboard";
				} else {
					model.addAttribute("msg", "Account can only be added once.");
					return "safe_user_success_dashboard";
				}
			} else {
				userAccountDto.setUser(userDto);
				userDto.setAccounts(userAccountDto);
				userService.updateUser(userDto);
				model.addAttribute("msg", "Account added successfully.");
				return "safe_user_success_dashboard";
			}

		}
	}

	@PostMapping("/digitalUserAccountDetails")
	public String digitalUserAccountDetails(@ModelAttribute UserAccountDto userAccountDto,
			@RequestParam("qr") MultipartFile qr, HttpSession httpSession, Model model) {
		try {
			userAccountDto.setQrCode(qr.getBytes());
		} catch (IOException e) {
			model.addAttribute("error", "Error uploading QR code.");
			return "error_page"; // Handle the error gracefully
		}
		UserDto userDto = userService.findUserById((String) httpSession.getAttribute("id"));
		UserAccountDto accounts = userDto.getAccounts();
		if (accounts != null) {
			if (accounts.getQrCode() == null || accounts.getUpiId() == null) {
				accounts.setUpiId(userAccountDto.getUpiId());
				accounts.setQrCode(userAccountDto.getQrCode());
				userDto.setAccounts(accounts);
				userService.updateUser(userDto);
				model.addAttribute("msg", "Account added successfully.");
				return "digital_user_success_dashboard";
			} else {
				model.addAttribute("msg", "Account can only be added once.");
				return "digital_user_success_dashboard";
			}
		} else {
			userAccountDto.setUser(userDto);
			userDto.setAccounts(userAccountDto);
			userService.updateUser(userDto);
			model.addAttribute("msg", "Account added successfully.");
			return "digital_user_success_dashboard";
		}

	}

	@PostMapping("/riskyUserAccountDetails")
	public String riskyUserAccountDetails(@Valid @ModelAttribute RiskyAccountDto riskyAccountDto,
			BindingResult bindingResult, Model model, HttpSession httpSession) {
		if (bindingResult.hasErrors()) {
			return "RiskyUserAccountDetails";
		} else {
			UserDto userDto = userService.findUserById((String) httpSession.getAttribute("id"));
			List<RiskyAccountDto> riskyAccounts = userDto.getRiskyAccounts();
			if(riskyAccounts.isEmpty()) {
				riskyAccounts.add(riskyAccountDto);
			}else {
				RiskyAccountDto riskyAccount = riskyAccounts.get(0);
				riskyAccount.setAccountHolderName(riskyAccountDto.getAccountHolderName());
				riskyAccount.setAccountNo(riskyAccountDto.getAccountNo());
				riskyAccount.setBankName(riskyAccountDto.getBankName());
				riskyAccount.setIfscCode(riskyAccountDto.getIfscCode());
				riskyAccounts.add(riskyAccount);
			}
			userDto.setRiskyAccounts(riskyAccounts);
			UserDto updateUser = userService.updateUser(userDto);

			model.addAttribute("msg", "Account added successfully.");
			return "risky_user_success_dashboard";
		}
	}

	@PostMapping("/optionsHandler")
	public String optionsHandler(@RequestParam String action, Model model, HttpSession httpSession) {
		UserDto userDto = userService.findUserById((String) httpSession.getAttribute("id"));
		if ("safe".equals(action)) {
			List<Order> orders = userService.findByOrderType("safe");
			double sumOfGetPoints = orders.stream().filter(order -> order.getStatus().equals("Approved"))
					.mapToDouble(Order::getGetPoints).sum();
			List<OrderRecord> orderRecords = userService.findOrderRecordByOrderType("safe");
			double sumOfInr = orderRecords.stream()
			    .filter(o -> o.getStatus().equals("Success"))
			    .mapToDouble(OrderRecord::getInr) // Assuming there is a getter method named getInr in the OrderRecord class
			    .sum();
			double sumOfPendingInr = orderRecords.stream()
				    .filter(o -> o.getStatus().equals("Pending"))
				    .mapToDouble(OrderRecord::getInr) // Assuming there is a getter method named getInr in the OrderRecord class
				    .sum();
			double sumOfPendingToSuccessInr = orderRecords.stream()
				    .filter(o -> o.getStatus().equals("PendingToSuccess"))
				    .mapToDouble(OrderRecord::getInr) // Assuming there is a getter method named getInr in the OrderRecord class
				    .sum();
			
			httpSession.setAttribute("accountBalance", sumOfGetPoints/*-sumOfInr-sumOfPendingInr*/);
			httpSession.setAttribute("pendingAccountBalance", sumOfPendingInr-sumOfPendingToSuccessInr);
			httpSession.setAttribute("accountType", "safe");
			return "safe_user_success_dashboard";
		} else if ("risky".equals(action)) {
			List<Order> orders = userService.findByOrderType("risky");
			double sumOfGetPoints = orders.stream().filter(order -> order.getStatus().equals("Approved"))
					.mapToDouble(Order::getGetPoints).sum();
			List<OrderRecord> orderRecords = userService.findOrderRecordByOrderType("risky");
			double sumOfInr = orderRecords.stream()
				    .filter(o -> o.getStatus().equals("Success"))
				    .mapToDouble(OrderRecord::getInr) // Assuming there is a getter method named getInr in the OrderRecord class
				    .sum();
				double sumOfPendingInr = orderRecords.stream()
					    .filter(o -> o.getStatus().equals("Pending"))
					    .mapToDouble(OrderRecord::getInr) // Assuming there is a getter method named getInr in the OrderRecord class
					    .sum();
				double sumOfPendingToSuccessInr = orderRecords.stream()
					    .filter(o -> o.getStatus().equals("PendingToSuccess"))
					    .mapToDouble(OrderRecord::getInr) // Assuming there is a getter method named getInr in the OrderRecord class
					    .sum();
				
				httpSession.setAttribute("accountBalance", sumOfGetPoints/*-sumOfInr-sumOfPendingInr*/);
				httpSession.setAttribute("pendingAccountBalance", sumOfPendingInr-sumOfPendingToSuccessInr);
			httpSession.setAttribute("accountType", "risky");
			return "risky_user_success_dashboard";
		} else if ("digital".equals(action)) {
			List<Order> orders = userService.findByOrderType("digital");
			double sumOfGetPoints = orders.stream().filter(order -> order.getStatus().equals("Approved"))
					.mapToDouble(Order::getGetPoints).sum();
			List<OrderRecord> orderRecords = userService.findOrderRecordByOrderType("digital");
			double sumOfInr = orderRecords.stream()
				    .filter(o -> o.getStatus().equals("Success"))
				    .mapToDouble(OrderRecord::getInr) // Assuming there is a getter method named getInr in the OrderRecord class
				    .sum();
				double sumOfPendingInr = orderRecords.stream()
					    .filter(o -> o.getStatus().equals("Pending"))
					    .mapToDouble(OrderRecord::getInr) // Assuming there is a getter method named getInr in the OrderRecord class
					    .sum();
				double sumOfPendingToSuccessInr = orderRecords.stream()
					    .filter(o -> o.getStatus().equals("PendingToSuccess"))
					    .mapToDouble(OrderRecord::getInr) // Assuming there is a getter method named getInr in the OrderRecord class
					    .sum();
				
				httpSession.setAttribute("accountBalance", sumOfGetPoints/*-sumOfInr-sumOfPendingInr*/);
				httpSession.setAttribute("pendingAccountBalance", sumOfPendingInr-sumOfPendingToSuccessInr);
			httpSession.setAttribute("accountType", "digital");
			return "digital_user_success_dashboard";
		} else if ("safeRecharge".equals(action)) {

			model.addAttribute("rechargeType", "Safe");
			return "recharge";
		}

		else if ("riskyRecharge".equals(action)) {
			model.addAttribute("rechargeType", "Risky");
			return "recharge";
		} else if ("digitalRecharge".equals(action)) {
			model.addAttribute("rechargeType", "Digital");
			return "recharge";
			
		} else if ("addRiskyAccount".equals(action)) {
			List<RiskyAccountDto> riskyAccounts = userDto.getRiskyAccounts();
			if(!riskyAccounts.isEmpty()) {
				model.addAttribute("accountDetails", riskyAccounts.get(0));
				return "RiskyUserSavedAccountDetails";
				}
			else {
				model.addAttribute("riskyAccountDto", new RiskyAccountDto());
				return "RiskyUserAccountDetails";
				}
		} else if ("addSafeAccount".equals(action)) {
			model.addAttribute("userAccountDto", new UserAccountDto());
			UserAccountDto accounts = userDto.getAccounts();
			if(accounts!=null) {
				if(accounts.getAccountNo()!=null) {
				model.addAttribute("accountDetails", accounts);
				}
				}
			return "SafeUserAccountDetails";
		} else if ("adddigitalAccount".equals(action)) {
			model.addAttribute("userAccountDto", new UserAccountDto());
			UserAccountDto accounts = userDto.getAccounts();
			if(accounts!=null) {
				if(accounts.getQrCode()!=null || accounts.getUpiId()!=null){
			model.addAttribute("accountDetails", accounts);
			String base64EncodedPaymentQr = Base64.getEncoder().encodeToString(accounts.getQrCode());
			model.addAttribute("accountDetailsQR", base64EncodedPaymentQr);
				}
			}
			return "DigitalUserAccountDetails";
		} else if ("uploadKyc".equals(action)) {
			model.addAttribute("userAccountDto", new UserAccountDto());
			return "kycform";
		} else if ("rechargeLog".equals(action)) {
			return "redirect:/rechargeLog";
		} else if ("orderRecord".equals(action)) {
			return "redirect:/orderRecord";
		} else {
			return null;
		}
	}

	@PostMapping(value = "/userhome")
	public String userHome() {
		return "accountType";
	}

	@GetMapping(value = "/getRate")
	@ResponseBody
	public ResponseEntity<Map<String, Double>> getRate(@RequestParam String accountType) {
		Rate rateById = userService.findRateById(1);
		if(rateById!=null) {
		double dollarRate = Double.parseDouble(userService.findRateById(1).getRateAmount());
		double percentage = userService.findPercentByAccountType(accountType).getPercentage();
		Map<String, Double> rateData = new HashMap<>();
		rateData.put("percentage", percentage);
		rateData.put("dollarRate", dollarRate);
		return ResponseEntity.ok(rateData);
		}else {
			return null;
		}

	}

	@PostMapping("/placeOrder")
	public String placeOrder(@ModelAttribute Order order, HttpSession httpSession, Model model) {
		order.setTradeNo(DateAndTimeProvider.getDateAndTime());
		order.setTopupFees(userService.findPercentByAccountType(order.getOrderType()).getPercentage());
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String formattedDate = formatter.format(date);
		order.setTradeTime(formattedDate);
		order.setRupeeDollarRate("1$ = ₹" + userService.findRateById(1).getRateAmount());
		Wallet wallet = userService.findWalletById(1);
		if(wallet!=null) {
		order.setPaymentQr(wallet.getQrCode());
		String base64EncodedPaymentQr = Base64.getEncoder().encodeToString(order.getPaymentQr());
		model.addAttribute("base64EncodedPaymentQr", base64EncodedPaymentQr);
		order.setWalletAddress(wallet.getWalletNo());
		model.addAttribute("order", order);
		httpSession.setAttribute("order", order);
		return "payment";
		}else {
			model.addAttribute("msg","Cannot perform recharge at the moment.");
			return order.getOrderType().toLowerCase()+"_user_success_dashboard";
		}
		
	}

	@GetMapping("/cancelOrder")
	public String cancelOrder(HttpSession httpSession, Model model) {
		Order order = (Order) httpSession.getAttribute("order");
		model.addAttribute("rechargeType", order.getOrderType());
		httpSession.removeAttribute("order");
		return "recharge";
	}

	@PostMapping("/submitOrder")
	public String submitOrder(HttpSession httpSession, Model model,
			@RequestParam("transferVoucher") MultipartFile transferVoucher) {
		Order order = (Order) httpSession.getAttribute("order");
		order.setStatus("Under review");
		try {
			order.setTransferVoucher(transferVoucher.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UserDto userDto = userService.findUserById((String) httpSession.getAttribute("id"));
		List<Order> orders = userDto.getOrders();
		orders.add(order);
		userDto.setOrders(orders);
		userService.updateUser(userDto);
		model.addAttribute("msg", "Order Successfully Placed.");
		httpSession.removeAttribute("order");
		return order.getOrderType().toLowerCase() + "_user_success_dashboard";
	}

	@PostMapping("/kycUpload")
	public String addKyc(HttpSession httpSession, @RequestParam("aadharFrontPage") MultipartFile aadharFrontPage,
			@RequestParam("aadharBackPage") MultipartFile aadharBackPage,
			@RequestParam("panCard") MultipartFile panCard, Model model) {
		String attribute = (String) httpSession.getAttribute("accountType");
		UserDto userDto = userService.findUserById((String) httpSession.getAttribute("id"));
		KycDetailsDto kycDetails = userDto.getKycDetails();
		if (kycDetails == null) {
			try {
				kycDetails = new KycDetailsDto();
				kycDetails.setUser(userDto);
				kycDetails.setAadharFrontPage(aadharFrontPage.getBytes());
				kycDetails.setAadharBackPage(aadharBackPage.getBytes());
				kycDetails.setPanCard(panCard.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			userDto.setKycDetails(kycDetails);
			userService.updateUser(userDto);
			model.addAttribute("msg", "Kyc details successfully uploaded");
			return attribute.toLowerCase() + "_user_success_dashboard";
		} else if (kycDetails != null) {
			model.addAttribute("msg", "Kyc details already uploaded");
			return attribute.toLowerCase() + "_user_success_dashboard";
		} else {
			model.addAttribute("msg", "Failed to upload Kyc");
			return attribute.toLowerCase() + "_user_success_dashboard";
		}

	}

	@GetMapping("/rechargeLog")
	public String rechargeLog(HttpSession httpSession, Model model) {
		UserDto userDto = userService.findUserById((String) httpSession.getAttribute("id"));
		String attribute = (String) httpSession.getAttribute("accountType");
		String string = StringUtil.capitalizeFirstLetter(attribute);
		List<Order> orders = userDto.getOrders();
		List<Order> orderList = orders.stream().filter(o -> o.getOrderType().equals(string))
				.peek(o -> o.setEncodedTransferVoucher(Base64.getEncoder().encodeToString(o.getTransferVoucher())))
				.collect(Collectors.toList());
		model.addAttribute("orderList", orderList);
		return "rechargelog";
	}

	@GetMapping("/orderRecord")
	public String orderRecord(HttpSession httpSession, Model model) {
		UserDto userDto = userService.findUserById((String) httpSession.getAttribute("id"));
		String attribute = (String) httpSession.getAttribute("accountType");
		String string = StringUtil.capitalizeFirstLetter(attribute);
		List<OrderRecord> orderRecords = userDto.getOrderRecords();
		List<OrderRecord> recordList = orderRecords.stream().filter(o -> o.getOrderType().equals(string))
				.peek(o -> o.setEncodedTransferVoucher(Base64.getEncoder().encodeToString(o.getTransferVoucher())))
				.collect(Collectors.toList());
		model.addAttribute("recordList", recordList);
		return "orderrecord";
	}
	
	@PostMapping("forgetPass")
	public String forgetPass(@ModelAttribute UserDto userDto,Model model,HttpServletRequest servletRequest) {
		UserDto userByEmail = userService.findUserByEmail(userDto.getEmail());
		if(userByEmail!=null) {
			StringBuffer url = servletRequest.getRequestURL();
			String url2 = url.substring(0, url.indexOf("/forgetPass"));
		EmailSender.sendPasswordResetLink(userByEmail.getEmail(), url2+"/resetPass?id="+userByEmail.getId());
		model.addAttribute("msg", "A password reset link has been successfully sent to your email address. Please check your inbox and spam folder, and follow the instructions to reset your password.");
		return "forgetPass";
		}else {
			model.addAttribute("msg", "The email address you entered is not registered. Please check and try again.");
			return "forgetPass";
		}
	}
	
	@GetMapping("resetPass")
	public String resetPass(@RequestParam("id") String id, @ModelAttribute UserDto userDto,Model model) {
			model.addAttribute("id",id);
			return "resetPass";
	}
	
	@PostMapping("resetPass")
	public String resetPassword(@ModelAttribute UserDto userDto,Model model) {
		UserDto userById = userService.findUserById(userDto.getId());
		userById.setPassword(userDto.getPassword());
		if(userById!=null) {
			userService.updatePass(userById);
			model.addAttribute("msg","Password reset successful.");
			model.addAttribute("loginDto",new LoginDto() );
		return "login";
		}else {
			model.addAttribute("loginDto",new LoginDto() );
			model.addAttribute("msg", "Password reset failed. Please try again later.");
			return "login";
		}
	}
	
	
	@PostMapping("changePassword")
	public String changePassword(@RequestParam String newPassword,  @ModelAttribute UserDto userDto,Model model) {
		try{
			UserDto userById = userService.findUserById(userDto.getId());
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(userById.getEmail(), userDto.getPassword()));
			userById.setPassword(newPassword);
			userService.updatePass(userById);
			model.addAttribute("msg", "Your password has been changed successfully.");
			model.addAttribute("loginDto",new LoginDto() );
		return "login";
		}catch(Exception exception) {
			model.addAttribute("msg", "The old password you entered is incorrect. Please try again.");
			return "changePassword";
		}
	}
	
	@GetMapping("/searchUser")
    public String searchUser(@RequestParam String query,HttpSession httpSession,Model model) {
        List<User> userByEmailOrMobile = userService.findUserByEmailOrMobile(query,query);
        if (!userByEmailOrMobile.isEmpty()) {
        	model.addAttribute("allusers", userByEmailOrMobile);
    		return "allusers"; // Redirect to a page showing the list of users after deletion
        } else {
        	model.addAttribute("allusers", userService.getAllUsers());
            return "allusers";
        }
    }
	
	@GetMapping("addRiskyAccount")
	public String addRiskyAccount(Model model) {
		model.addAttribute("riskyAccountDto", new RiskyAccountDto());
		return "RiskyUserAccountDetails";
	}
	
	
//	 @Autowired
//	    private HttpSession httpSession;
//
//	    // This method checks if the user is logged in before allowing access to certain pages
//	    private boolean isLoggedIn() {
//	        return httpSession.getAttribute("userId") != null;
//	    }
//
//	    // This method invalidates the session upon logout
//	    @GetMapping("/logout")
//	    public String logout() {
//	        httpSession.invalidate();
//	        return "redirect:/login";
//	    }
//
//	    // This method redirects users to the login page if they are not logged in
//	    @GetMapping("/restrictedPage")
//	    public String restrictedPage(Model model) {
//	        if (!isLoggedIn()) {
//	            return "redirect:/login";
//	        }
//	        // Proceed to serve the restricted page
//	        return "restrictedPage";
//	    }
	
	
	
}
