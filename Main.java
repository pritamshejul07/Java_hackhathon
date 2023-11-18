import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	public  static Scanner sc;
	private static Integer INC = 6;
	private static Integer RVI = 11;
	
	public static void userOps() {
		int choice;
		do {
			System.out.print("\n0. Exit \n1. Sign Up\n2. Sign In\nEnter choice: ");
			choice = sc.nextInt();
			switch (choice) {
				case 0: 
					System.out.println("Bye!");
					break;
				case 1: 
					signUp();
					break;
				case 2: 
					User user = signIn();
//					System.out.println(user.toString());
					userAppUse(user);
					break;
			}
		} while (choice != 0);
	}
	
	public static void userAppUse(User user) {
		int choice;
		do {
			System.out.print("\n0. Log Out \n1. Edit Profile\n2. Change Password\n3. Write Review \n4. Edit Review \n5. Display All Movies \n6. Display All Reviews\n7. Display  My Reviews \n8. Display Reviews Shared With me\n9. Share a Review\n10. Delete a Review");
			choice = sc.nextInt();
			switch (choice) {
				case 0 :
					System.out.println("------------------------------------");
					System.out.println("Bye! Logged Out!");
					break;
				case 1 :
					try (UserDao userDao = new UserDao()) {
						user.editUser();
						userDao.editProfile(user);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case 2 : 
					try (UserDao userDao = new UserDao()) {
						System.out.println("Please enter the new Password");
						Scanner s1 = new Scanner(System.in);
						String pass = s1.next();
						user.setPassword(pass);
						userDao.changePassword(user);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case 3 :
					try (MovieDao mDao = new MovieDao()) {
						List<Movies> list = mDao.getAllMovies();
						System.out.println("==================================================");
						for (Movies m : list)
							System.out.println(m.toString());
						System.out.println("==================================================");
						
						Reviews review = writeReview(user.getId());
						try (ReviewsDao rDao = new ReviewsDao()) {
							rDao.save(review);
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					break;
				case 4 :
					try (ReviewsDao rDao = new ReviewsDao()) {
						List<Reviews> list = rDao.getAllReviewsByUserId(user.getId());
						System.out.println("==================================================");
						for (Reviews r : list)
							System.out.println(r.toString());
						System.out.println("==================================================");
						Reviews r = editReview(user.getId());
						rDao.editReview(r);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case 5 :
					try (MovieDao mDao = new MovieDao()) {
						List<Movies> list = mDao.getAllMovies();
						System.out.println("==================================================");
						for (Movies m : list)
							System.out.println(m.toString());
						System.out.println("==================================================");
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case 6 :
					try (ReviewsDao rDao = new ReviewsDao()) {
						List<Reviews> list = rDao.getAllReviews();
						System.out.println("==================================================");
						for (Reviews r : list)
							System.out.println(r.toString());
						System.out.println("==================================================");
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case 7 :
					try (ReviewsDao rDao = new ReviewsDao()) {
						List<Reviews> list = rDao.getAllReviewsByUserId(user.getId());
						System.out.println("==================================================");
						for (Reviews r : list)
							System.out.println(r.toString());
						System.out.println("==================================================");
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case 8 :
					try (ReviewsDao rDao = new ReviewsDao()) {
						List<Reviews> list = rDao.getAllReviewsSharedWithMe(user.getId());
						System.out.println("==================================================");
						for (Reviews r : list)
							System.out.println(r.toString());
						System.out.println("==================================================");
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case 9 :
					try (ReviewsDao rDao = new ReviewsDao()) {
						List<Reviews> list = rDao.getAllReviews();
						System.out.println("==================================================");
						for (Reviews r : list)
							System.out.println(r.toString());
						System.out.println("==================================================");
					} catch (Exception e) {
						e.printStackTrace();
					}
					System.out.println("Enter the review id that you want to share");
					int reviewId = sc.nextInt();
					try (UserDao rDao = new UserDao()) {
						List<User> list = rDao.getAllUsers();
						System.out.println("==================================================");
						for (User r : list)
							System.out.println(r.toString());
						System.out.println("==================================================");
					} catch (Exception e) {
						e.printStackTrace();
					}
					sc.nextLine();
					System.out.print("Enter the user id you want to share review with : ");
					String s1 = sc.nextLine();
					System.out.println(s1);
					String[] arr = s1.split(" ");
					List<Integer> list = new ArrayList<>();
					for (String s : arr)
						list.add(Integer.valueOf(s));
						try (ShareDao rDao = new ShareDao()) {
							for (Integer i : list) {
								if (i != user.getId()) {
									Share share = new Share(reviewId, i);
									rDao.save(share);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					
					break;
					
				case 10 :
					try (ReviewsDao rDao = new ReviewsDao()) {
						List<Reviews> list1 = rDao.getAllReviewsByUserId(user.getId());
						System.out.println("==================================================");
						for (Reviews r : list1)
							System.out.println(r.toString());
						System.out.println("==================================================");
						System.out.println("Enter the review id that you want to delete :");
						Integer rI = sc.nextInt();
						rDao.delete(rI);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
			}
		} while (choice != 0);
	}
	
	public static User signIn() {
		String email;
		String password;
		System.out.print("Enter email: ");
		email = sc.next();
		System.out.print("Enter password: ");
		password = sc.next();
		try (UserDao dao = new UserDao()) {
			User user = dao.findByEmail(email);
			if (user != null && password.equals(user.getPassword()))
				return user;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Reviews editReview(Integer id) {
		Reviews r = new Reviews();
		System.out.println("Enter the review id of review to be edited");
		Integer rvId = sc.nextInt();
		System.out.println("Write New your review");
		sc.nextLine();
		String r1 = sc.nextLine();
		System.out.println("Enter New rating for the movie");
		Integer rating = sc.nextInt();
		r.setReviewId(rvId);
		r.setReview(r1);
		r.setRating(rating);
		r.setUserId(id);
		return r;
	}
	
	public static Reviews writeReview(Integer id) {
		System.out.println("Enter the Movie id form above list to give a review");
		Integer movieId = sc.nextInt();
		System.out.println("Write your review");
		sc.nextLine();
		String r1 = sc.nextLine();
		System.out.println("Enter your rating for the movie");
		Integer rating = sc.nextInt();
		Reviews r = new Reviews();
		r.setReviewId(RVI);
		r.setMovieId(movieId);
		r.setReview(r1);
		r.setRating(rating);
		r.setUserId(id);
		RVI++;
		System.out.println(r.toString());
		return r;
	}
	
	
	public static void signUp() {
		Integer userId = INC++;
		System.out.print("First Name: ");
		String fname = sc.next();
		System.out.print("Last Name: ");
		String lname = sc.next();
		System.out.print("Email: ");
		String email = sc.next();
		if (!validateEmail(email)) {
			System.out.println("Email  is not valid !");
			return;
		}
		System.out.print("Password: ");
		String passwd = sc.next();
		 if (passwd.length() < 6) {
	            System.out.println("Error: Password should be at least 6 characters long.");
	            return;
	    }
		System.out.println("Enter Mobile Number");
		String mobileNo = sc.next();
		System.out.print("Birth Date (dd-MM-yyyy): ");
		String dateStr = sc.next();
		if (!isValidDate(dateStr)) {
			System.out.println("Email  is not valid !");
			return;
		}
		User user = new User(INC, fname, lname, email, passwd, mobileNo, DateUtil.parse(dateStr));
		try (UserDao dao = new UserDao()) {
			int count = dao.save(user);
			System.out.println("User Added " + count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static boolean validateEmail(String email) {
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
	
	private static boolean isValidDate(String dateStr) {
        String dateFormat = "\\d{2}-\\d{2}-\\d{4}";
        return dateStr.matches(dateFormat);
    }
	
	public static void main(String[] args) {
		sc = new Scanner(System.in);
		userOps();
		sc.close();
	}
	
}
