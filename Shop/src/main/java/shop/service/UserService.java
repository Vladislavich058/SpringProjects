package shop.service;

import shop.domain.User;

public interface UserService {
	boolean checkExistUsername(String username);
	
	User addClient(User user);
	
	boolean checkExistUsernameEdit(User editUser, User authUser);
	
	User editUser(User editUser, User authUser);
	
	void deleteUser(Long id);
}
