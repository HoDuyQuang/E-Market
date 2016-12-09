package vn.edu.dut.itf.e_market.api;

public class APIConfig {
//	private static final String DOMAIN = "http://127.0.0.1";
	private static final String DOMAIN = "http://192.168.1.167";
	private static final String PATH = "/emarket/public/api/";
	private static final String BASE_URL = DOMAIN + PATH;

	public static final String URL_29_GET_LIST_PROVINCES = BASE_URL + "requests/init";

	public static final String URL_1_GET_HOME_PAGE_API = BASE_URL + "requests/getHome";
	public static final String URL_2_GET_RESTAURANT_INFO = BASE_URL + "Restaurants/getRestaurantInfo";
	public static final String URL_3_GET_LIST_PHOTO_RESTAURANT = BASE_URL + "restaurants/getListPhotosRestaurant";

	public static final String URL_5_GET_LIST_MENU = BASE_URL + "foods/getListMenu";
	public static final String URL_6_GET_PROFILE = BASE_URL + "users/getProfile";
	public static final String URL_7_POST_PROFILE = BASE_URL + "users/postProfile";
	public static final String URL_8_GET_REVIEW_HISTORY = BASE_URL + "reviews/getReviewHistory";
	public static final String URL_9_POST_REVIEW_RESTAURANT = BASE_URL + "reviews/postReviewRestaurant";
	public static final String URL_10_POST_REVIEW_FOOD = BASE_URL + "reviews/postReviewFood";
	public static final String URL_11_GET_LIST_REVIEW_RESTAURANT = BASE_URL
			+ "reviews/getListReviewRestaurants";
	public static final String URL_12_GET_LIST_REVIEW_FOOD = BASE_URL
			+ "reviews/getListReviewFoods";
	public static final String URL_13_GET_REVIEW_FOOD_DETAIL = BASE_URL + "reviews/getReviewFoodDetail";
	public static final String URL_14_GET_REVIEW_RESTAURANT_DETAIL = BASE_URL + "reviews/getReviewRestaurantDetail";
	public static final String URL_15_SET_LIKE_REVIEW_FOOD = BASE_URL + "reviews/setLikeReviewFood";
	public static final String URL_16_POST_COMMENT_RESTAURANT = BASE_URL + "reviews/postCommentRes";
	public static final String URL_17_GET_LIST_FOOD = BASE_URL + "orders/getListFoods";
	public static final String URL_18_GET_FEE = BASE_URL + "requests/getFee";

	public static final String URL_19_GET_LIST_FAVOURITE = BASE_URL + "foods/getListFavorites";
	public static final String URL_20_SET_FAVOURITE_FOOD = BASE_URL + "foods/setFavoriteFood";
	public static final String URL_21_GET_LIST_NOTIFICATION = BASE_URL + "notifications/getListNotification";
	public static final String URL_22_COUNT_NOTIFY_NEW = BASE_URL + "notifications/getCountNotificationNew";
	public static final String URL_23_SET_NOTIFY_RES_READ = BASE_URL + "notifications/setNotificationReadStatusRest";
	public static final String URL_24_POST_FEEDBACK = BASE_URL + "requests/postFeedback";
	public static final String URL_25_GET_DELIVERY_PROCESS = BASE_URL + "requests/getDeliveryProcess";

	public static final String URL_28_POST_ORDER = BASE_URL + "orders/postOrder";

	public static final String URL_30_POST_CHANGE_PASS = BASE_URL + "users/postChangePassword";
	public static final String URL_31_LOGIN = BASE_URL+"users/postLogin";
	public static final String URL_32_POST_REGISTER = BASE_URL+ "users/postRegister";
	public static final String URL_33_SENT_FORGOT_PASS = BASE_URL + "users/sendForgotPassword";
	public static final String URL_34_GET_LOGOUT = BASE_URL + "users/getLogout";
	public static final String URL_35_GET_LIST_ORDER = BASE_URL + "orders/getListOrders";
	public static final String URL_36_GET_ORDER_DETAIL = BASE_URL + "orders/getOrderDetail";
	public static final String URL_37_GET_FOOD_DETAIL = BASE_URL + "foods/getFoodDetail";
	public static final String URL_38_GET_LIST_PHOTO_FOOD = BASE_URL + "foods/getListPhotoFoods";
	public static final String URL_39_SET_NOTIFY_YOU_READ = BASE_URL + "notifications/setNotificationReadStatusYou";
	public static final String URL_40_SET_DEVICE_TOKEN = BASE_URL + "users/setDeviceToken";
	public static final String URL_41_LIKE_RESTAURANT_REVIEW = BASE_URL + "reviews/setLikeReviewRes";
	public static final String URL_42_POST_COMMENT_FOOD = BASE_URL + "reviews/postCommentFood";
	public static final String URL_43_LIKE_RESTAURANT_REVIEW_COMMENT = BASE_URL + "reviews/setLikeCommentRes";
	public static final String URL_44_LIKE_FOOD_REVIEW_COMMENT = BASE_URL + "reviews/setLikeCommentFood";
	public static final String URL_45_POST_EMAIL = BASE_URL+ "users/postEmail";
	public static final String URL_46_POST_NOTIFICATION = BASE_URL+ "notifications/postNotification";
	public static final String URL_47_SET_NOTIFICATION_NEW_TO_OLD = BASE_URL+ "notifications/setNotificationNewToOld";
	public static final String URL_48_GET_SUGGEST = BASE_URL+ "foods/getListSuggest";

}
