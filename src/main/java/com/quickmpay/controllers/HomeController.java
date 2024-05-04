package com.quickmpay.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.quickmpay.dtos.LoginDto;
import com.quickmpay.dtos.UserAccountDto;
import com.quickmpay.dtos.UserDto;
import com.quickmpay.entities.User;



@Controller
public class HomeController {
	
	
	@GetMapping(value="/")
	public String homePage() {
		return "home";
	}
	
	@GetMapping(value="/contact")
	public String contact() {
		return "contact";
	}
	
	@GetMapping("changePass")
	public String changePass() {
			return "changePassword";
	}
	
	@GetMapping(value="/recharge")
	public String recharge() {
		
		return "recharge";
	}
	
	@GetMapping(value="/forgetPass")
	public String forgetPass() {
		return "forgetPass";
	}
	
//	@GetMapping(value="/resetPass")
//	public String resetPass() {
//		return "resetPass";
//	}
	
	
	@GetMapping(value="/signin")
	public String loginPage(Model model) {
		model.addAttribute("loginDto",new LoginDto() );
		return "login";
	}
	
	@GetMapping("/signup")
	public String signupPage(Model model) {
		model.addAttribute("userDto",new UserDto());
		return "signup";
	}
	
	@GetMapping("/about")
	public String aboutPage() {
		return "about";
	}
	
	@GetMapping("/verifyPage")
	public String verifyPage() {
		return "verify";
	}

  @GetMapping("/userDashboard")
  public  String userDashboard(){
     return  "user_dashboard";
  }
  
  @GetMapping("/accountType")
  public String accountType() {
	  return "accountType";
  }
  
  @GetMapping("/safeUserSuccessDashboard")
  public  String userSuccessDashboard(){
     return  "safe_user_success_dashboard";
  }
  
  @GetMapping("/riskyUserSuccessDashboard")
  public  String riskyUserSuccessDashboard(){
     return  "risky_user_success_dashboard";
  }
  
  @GetMapping("/payment")
  public  String payment(){
	 
     return  "payment";
  }
  
  @GetMapping("/accountsDetails")
  public String accountDetails(Model model){
	 model.addAttribute("userAccountDto",new UserAccountDto());
     return "UserAccountDetails";
  }
  
  @GetMapping("/kycForm")
  public String kycForm() {
	  return "kycform";
  }
  
	
}
