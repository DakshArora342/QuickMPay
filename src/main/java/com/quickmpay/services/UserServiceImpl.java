package com.quickmpay.services;


import java.util.*;import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.quickmpay.dtos.LoginDto;
import com.quickmpay.dtos.RoleDto;
import com.quickmpay.dtos.UserDto;
import com.quickmpay.entities.Order;
import com.quickmpay.entities.OrderRecord;
import com.quickmpay.entities.Percentage;
import com.quickmpay.entities.Rate;
import com.quickmpay.entities.Role;
import com.quickmpay.entities.Tips;
import com.quickmpay.entities.User;
import com.quickmpay.entities.Wallet;
import com.quickmpay.extras.EmailSender;
import com.quickmpay.extras.OTPGenerator;
import com.quickmpay.repos.OrderRecordRepo;
import com.quickmpay.repos.OrderRepo;
import com.quickmpay.repos.PercentageRepo;
import com.quickmpay.repos.RateRepo;
import com.quickmpay.repos.TipsRepo;
import com.quickmpay.repos.UserRepo;
import com.quickmpay.repos.WalletRepo;

@Service
public class UserServiceImpl implements UserService {

	UserRepo userRepo;
	ModelMapper modelMapper;
	
	PasswordEncoder passwordEncoder;
	RateRepo rateRepo;
	PercentageRepo percentageRepo;
	WalletRepo walletRepo;
	TipsRepo tipsRepo;
	OrderRepo orderRepo;
	OrderRecordRepo  orderRecordRepo;
	public UserServiceImpl(UserRepo userRepo,OrderRepo orderRepo, ModelMapper modelMapper, PasswordEncoder passwordEncoder,RateRepo rateRepo,OrderRecordRepo  orderRecordRepo,PercentageRepo percentageRepo,TipsRepo tipsRepo,	WalletRepo walletRepo
) {
		super();
		this.userRepo = userRepo;
		this.modelMapper = modelMapper;
		this.passwordEncoder = passwordEncoder;
		this.rateRepo=rateRepo;
		this.percentageRepo=percentageRepo;
		this.walletRepo=walletRepo;
		this.tipsRepo=tipsRepo;
		this.orderRepo=orderRepo;
		this.orderRecordRepo=orderRecordRepo;
	}

	@Override
	public UserDto addUser(UserDto userDto) {
		User user = null;
		List<User> list = userRepo.findByEmailOrMobile(userDto.getEmail(),userDto.getMobile());
		if (!list.isEmpty()) {
			user = list.get(0);
		}
//		User user = userRepo.findByEmail(userDto.getEmail());
		if (user == null) {
			String otp = OTPGenerator.generateOTP();
			EmailSender.sendOtp(userDto.getEmail(), otp);
			userDto.setOtp(otp);
			RoleDto roleDto =new RoleDto();
			roleDto.setName("USER");
			HashSet<RoleDto> hashSet=new HashSet<>();
			hashSet.add(roleDto);
			userDto.setRoles(hashSet);
			userDto.setStatus("inactive");
			userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
			User user2 = userRepo.save(modelMapper.map(userDto, User.class));
			return modelMapper.map(user2, UserDto.class);
		} else
			return null;
	}
	@Override
	public UserDto addAdmin(UserDto userDto) {
		User user = null;
		Optional<User> optional = userRepo.findByEmail(userDto.getEmail());
		if (optional.isPresent()) {
			user = optional.get();
		}
//		User user = userRepo.findByEmail(userDto.getEmail());
		if (user == null) {
			String otp = OTPGenerator.generateOTP();
			EmailSender.sendOtp(userDto.getEmail(), otp);
			userDto.setOtp(otp);
			RoleDto roleDto =new RoleDto();
			roleDto.setName("ADMIN");
			HashSet<RoleDto> hashSet=new HashSet<>();
			hashSet.add(roleDto);
			userDto.setRoles(hashSet);
			userDto.setStatus("active");
			userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
			User user2 = userRepo.save(modelMapper.map(userDto, User.class));
			return modelMapper.map(user2, UserDto.class);
		} else
			return null;
	}

	
	@Override
	public String findUser(LoginDto loginDto) {
		User user = null;
		Optional<User> optional = userRepo.findByEmail(loginDto.getEmail());
		if (optional.isPresent()) {
			user=optional.get();
		}
			if (user.getStatus().equals("active")) {
				return "active";
			} else
				return "inactive";
		
	}

	@Override
	public String verify(String email, String otp) {
		User user = userRepo.findByEmailAndOtp(email, otp);
		if (user != null) {
			user.setStatus("active");
			userRepo.save(user);
			return "active";
		}
		return null;
	}

	@Override
	public LoginDto resendOtp(LoginDto loginDto) {
		User user;
		Optional<User> optional = userRepo.findByEmail(loginDto.getEmail());
		if(optional.isPresent()) {
			user=optional.get();
			String otp = OTPGenerator.generateOTP();
			EmailSender.sendOtp(loginDto.getEmail(), otp);
			user.setOtp(otp);
			User user2 = userRepo.save(user);
			return modelMapper.map(user2, LoginDto.class);
		}
		return null;
	}

	@Override
	public String forgetPass(String email) {
//		User user;
//		Optional<User> optional = userRepo.findByEmail(email);
//		if(optional.isPresent()) {
//			user=optional.get();
//			EmailSender.sendPassword(user.getEmail(),passwordEncoder  user.getPassword());
//		}
		return null;
	}

	@Override
	public UserDto findUserByEmail(String email) {
		User user=null;
		Optional<User> optional = userRepo.findByEmail(email);
		if(optional.isPresent()) {
			user=optional.get();
			return modelMapper.map(user, UserDto.class);
		}
		return null;
	}

	@Override
	public UserDto findUserById(String id) {
		return modelMapper.map(userRepo.findById(id), UserDto.class);
	}

	@Override
	public UserDto updateUser(UserDto userDto) {
		User user = modelMapper.map(userDto, User.class);
		User user2 = userRepo.save(user);
		UserDto userDto2 = modelMapper.map(user2, UserDto.class);
		return userDto2;
	}
	
	@Override
	public UserDto updatePass(UserDto userDto) {
		User user = modelMapper.map(userDto, User.class);
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		User user2 = userRepo.save(user);
		UserDto userDto2 = modelMapper.map(user2, UserDto.class);
		return userDto2;
	}

	@Override
	public Rate findRateById(int id) {
		Optional<Rate> optional = rateRepo.findById(id);
		if(optional.isPresent()) {
		return optional.get();
		}
		return null;		
	}

	@Override
	public Percentage findPercentByAccountType(String type) {
		
		return percentageRepo.findByAccountType(type);
	}

	@Override
	public Wallet findWalletById(int id) {
		Optional<Wallet> optional =walletRepo.findById(id);
		if(optional.isPresent()) {
		return optional.get();
		}
		return null;
	}

	@Override
	public Tips findTipsById(int id) {
		Optional<Tips> optional =tipsRepo.findById(id);
		if(optional.isPresent()) {
		return optional.get();
		}
		return null;
	}

	@Override
	public List<Order> findByOrderType(String orderType) {
		return orderRepo.findByOrderType(orderType);
	}

	@Override
	public List<UserDto> getAllUsers() {
		List<User> usersWithOnlyUserRole = userRepo.findAll().stream()
			    .filter(u -> u.getRoles().stream()
			        .map(Role::getName)
			        .collect(Collectors.joining(", "))
			        .equalsIgnoreCase("USER")
			    )
			    .collect(Collectors.toList());
		return usersWithOnlyUserRole.stream().map(u->modelMapper.map(u, UserDto.class)).collect(Collectors.toList());
	}

	@Override
	public List<Order> getAllOrders() {
		// TODO Auto-generated method stub
		return orderRepo.findAll();
	}

	@Override
	public void deleteUserById(String id) {
		userRepo.deleteById(id);
	}

	@Override
	public List<User> findByOrders(List<Order> orders) {
		
		return userRepo.findByOrders(orders);
	}

	@Override
	public User findByOrder(Order orders) {
		 ArrayList<Order> arrayList = new ArrayList<>();
		arrayList.add(orders);
		List<User> user = userRepo.findByOrders(arrayList);
		return user.get(0);
	}
	
	@Override
	public List<UserDto> getAllUsers(int pageNo) {
    Pageable pageable  = PageRequest.of(pageNo, 10);
       return  userRepo.findAll(pageable).get().collect(Collectors.toList())
         .stream().map(u->modelMapper.map(u,UserDto.class)).collect(Collectors.toList());
//    return userRepo.findAll().stream().map(u->modelMapper.map(u, UserDto.class)).collect(Collectors.toList());
	}

	@Override
	public List<OrderRecord> findOrderRecordByOrderType(String orderType) {
		
		return orderRecordRepo.findByOrderType(orderType);
	}

	@Override
	public List<User> findUserByEmailOrMobile(String email, String mobile) {
		// TODO Auto-generated method stub
		return userRepo.findByEmailOrMobile(email, mobile);
	}

}
