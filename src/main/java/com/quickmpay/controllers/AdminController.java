package com.quickmpay.controllers;

import java.io.IOException;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.quickmpay.dtos.KycDetailsDto;
import com.quickmpay.dtos.UserDto;
import com.quickmpay.entities.Order;
import com.quickmpay.entities.OrderRecord;
import com.quickmpay.entities.Percentage;
import com.quickmpay.entities.Rate;
import com.quickmpay.entities.Tips;
import com.quickmpay.entities.User;
import com.quickmpay.entities.Wallet;
import com.quickmpay.services.AdminService;
import com.quickmpay.services.OrderService;
import com.quickmpay.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller

public class AdminController {

	AdminService adminService;
	UserService userService;
	OrderService orderService;
	ModelMapper modelMapper;
	AdminController(AdminService adminService, UserService userService,OrderService orderService,ModelMapper modelMapper) {
		this.adminService = adminService;
		this.userService = userService;
		this.orderService=orderService;
		this.modelMapper=modelMapper;
	}

	@GetMapping("/admin")
	public String admin() {
		return "admin";
	}

	
	@GetMapping("/approveOrder")
	public String approveOrder(@RequestParam("id") String userId, Model model) {
		Order order = orderService.findOrderById(userId);
		order.setStatus("Approved");
		orderService.saveOrder(order);
		User user = userService.findByOrder(order);
		List<Order> orders = user.getOrders();
		List<Order> orderList = orders.stream().map(o -> {
			o.setEncodedTransferVoucher(Base64.getEncoder().encodeToString(o.getTransferVoucher()));
			return o;
		}).collect(Collectors.toList());
		model.addAttribute("allorders", orderList);
		return "allorders";
		
	}
	
	@GetMapping("/rejectOrder")
	public String rejectOrder(@RequestParam("id") String userId, Model model) {
		Order order = orderService.findOrderById(userId);
		order.setStatus("Rejected");
		orderService.saveOrder(order);
		User user = userService.findByOrder(order);
		List<Order> orders = user.getOrders();
		List<Order> orderList = orders.stream().map(o -> {
			o.setEncodedTransferVoucher(Base64.getEncoder().encodeToString(o.getTransferVoucher()));
			return o;
		}).collect(Collectors.toList());
		model.addAttribute("allorders", orderList);
		return "allorders";
		
	}
	
	@GetMapping("/viewKyc")
	public String viewKyc(@RequestParam("id") String userId, Model model) {
		UserDto userDto = userService.findUserById(userId);
		KycDetailsDto kycDetails = userDto.getKycDetails();
		String  aadharFrontPage= Base64.getEncoder().encodeToString( kycDetails.getAadharFrontPage());
		String aadharBackPage = Base64.getEncoder().encodeToString(kycDetails.getAadharBackPage());
		String pancard = Base64.getEncoder().encodeToString(kycDetails.getPanCard());
		List<String> list = List.of(aadharFrontPage,aadharBackPage,pancard);
		model.addAttribute("kyc", list);
		return "kycdetails";
	}
	
	@GetMapping("/viewOrders")
	public String viewOrders(@RequestParam("id") String userId, Model model) {
		UserDto userDto = userService.findUserById(userId);
		List<Order> orders = userDto.getOrders();
		List<Order> orderList = orders.stream().map(o -> {
			o.setEncodedTransferVoucher(Base64.getEncoder().encodeToString(o.getTransferVoucher()));
			return o;
		}).collect(Collectors.toList());
		model.addAttribute("allorders", orderList);
		return "allorders";
	}

	@PostMapping("/handlePage")
	public String verify(@RequestParam String action,Model model) {
		if ("addrate".equals(action)) {

			return "addRate";

		} else if ("addWallet".equals(action)) {

			return "addWallet";
		} else if ("addPercentage".equals(action)) {
			return "addPercentage";
		} else if ("addTips".equals(action)) {
			return "addTips";
		} else if ("viewUsers".equals(action)) {
        	model.addAttribute("allusers", userService.getAllUsers());
			return "allusers";
		} 
		return action;

	}

	@PostMapping(value = "/addrate")
	public String addRateData(@ModelAttribute Rate rate) {
		adminService.addRate(rate);
		return "admin";
	}

	@PostMapping(value = "/addWallet")
	public String addWallet(@ModelAttribute Wallet wallet, @RequestParam("qr") MultipartFile qr) {
		try {
			wallet.setQrCode(qr.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		adminService.addWallet(wallet);
		return "admin";
	}

	@PostMapping(value = "/addPercentage")
	public void addPercentage(@ModelAttribute Percentage percentage) {
		adminService.addPercentage(percentage);
	}

	@PostMapping(value = "/adminhome")
	public String userHome() {
		return "admin";
	}

	@PostMapping(value = "/addTips")
	public String addTips(@ModelAttribute Tips tips) {
		adminService.addTip(tips);
		return "admin";
	}

	@GetMapping("/deleteUser")
	public String deleteUser(@RequestParam("id") String userId, Model model) {
		userService.deleteUserById(userId);
    	model.addAttribute("allusers", userService.getAllUsers());
		return "allusers"; // Redirect to a page showing the list of users after deletion
	}
	
	@PostMapping("/transferAmount")
	public String transferAmount(@RequestParam String paymentType ,@ModelAttribute OrderRecord orderRecord,Model model) {
		Order order = orderService.findByTradeNo(orderRecord.getTradeNo());
		User user = userService.findByOrder(order);
		List<Order> orders = user.getOrders();
		List<Order> list = orders.stream().filter(o->o.getTradeNo().equals(orderRecord.getTradeNo())).collect(Collectors.toList());
		orders.remove(list.get(0));
		double getPoints;
		double inr;
		double pendingPoints;
		if(paymentType.equals("Success")){
			getPoints = order.getGetPoints();
			inr = orderRecord.getInr();
			if(inr<=getPoints) {
				getPoints-=inr;
				order.setGetPoints(getPoints);
				orders.add(order);
				user.setOrders(orders);
				userService.updateUser(modelMapper.map(user, UserDto.class));
			}
		}else if(paymentType.equals("Pending")) {
			getPoints = order.getGetPoints();
			inr = orderRecord.getInr();
			if(inr<=getPoints) {
				getPoints-=inr;
				order.setGetPoints(getPoints);
				order.setPendingPoints(inr);
				orders.add(order);
				user.setOrders(orders);
				userService.updateUser(modelMapper.map(user, UserDto.class));
			}
		}else if(paymentType.equals("PendingToSuccess")) {
			pendingPoints = order.getPendingPoints();
			inr = orderRecord.getInr();
			if(inr<=pendingPoints) {
				pendingPoints-=inr;
				order.setPendingPoints(pendingPoints);
				orders.add(order);
				user.setOrders(orders);
				userService.updateUser(modelMapper.map(user, UserDto.class));
			}
		}else {
				
			}
		List<OrderRecord> orderRecords = user.getOrderRecords();
		OrderRecord orderRecord2 = modelMapper.map(order, OrderRecord.class);
		orderRecord2.setInr(orderRecord.getInr());
		orderRecord2.setUtr(orderRecord.getUtr());
		orderRecord2.setStatus(paymentType);
		orderRecords.add(orderRecord2);
		user.setOrderRecords(orderRecords);
		userService.updateUser(modelMapper.map(user, UserDto.class));
//    	model.addAttribute("allusers", userService.getAllUsers());
//		return "allusers"; // Redirect to a page showing the list of users after deletion
		model.addAttribute("allorders", orders);
		return "allorders";
	}
	
	@GetMapping("changePassAdmin")
	public String changePass() {
			return "changePasswordAdmin";
	}
	
	@GetMapping("/allUsers")
	public String allUsers(Model model) {
    	model.addAttribute("allusers", userService.getAllUsers());
		return "allusers";
	}

}
