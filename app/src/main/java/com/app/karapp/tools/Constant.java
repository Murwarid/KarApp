package com.app.karapp.tools;

public class Constant {

    public static final String LOGGED_IN_PREF = "logged_in_status";
    public static final String USER_ID = "id";
    public static final String USERNAME = "phone";
    public static final String NAME = "name";
    public static final String LASTNAME = "last_name";
    public static final String USER_PASSWORD = "password";
    public static final String USER_PROFILE_PIC = "image";
    public static final String PASSWORD = "password";
    public static final String PHONE_NUMBER = "phone";
    public static final int PICK_IMAGE = 1;
    public static final int REQUEST_GET_SINGLE_FILE = 1;

    public static final String SUCCCESS = "success";

    public static final String IP ="192.168.1.101";
    public static final String BASE_URL ="http://" + IP + "/NewBlog/public/";

    public static final String USER_REGISTER ="http://" + IP + "/NewBlog/public/registeruser";
    public static final String LOGIN ="http://" + IP + "/NewBlog/public/loginuser";
    public static final String GET_PROVINCE ="http://" + IP + "/NewBlog/public/getprovince";
    public static final String IS_EXIST_EMAIL_PHONE = "http://" + IP + "/NewBlog/public/PhoneExist";
    public static final String GET_POSTS ="http://" + IP + "/NewBlog/public/getPosts";
    public static final String SINGL_POST = "http://" + IP + "/NewBlog/public/getSinglePost/{id}";
    public static final String POST_BY_PROVINCE ="http://" + IP + "/NewBlog/public/Searchbyprovinces/{id}";

    public static final String GET_CATEGORY="http://" + IP + "/NewBlog/public/getcategory";
    public static final String POST_BY_CATEGORY="http://" + IP + "/NewBlog/public/SearchByCategory/{id}";
    public static final String POST_BY_CP="http://" + IP + "/NewBlog/public/SearchByCP";

    public static final String ADD_EXPERIENCES ="http://" + IP + "/NewBlog/public/addexperiences";
    public static final String ADD_Skills ="http://" + IP + "/NewBlog/public/addSkill";

    public static final String UPDATE_Skills ="http://" + IP + "/NewBlog/public/update_user_skill";
    public static final String DELETE_Skills ="http://" + IP + "/NewBlog/public/delete_user_skill/{id}";

    public static final String UPDATE_EXPERIENCE ="http://" + IP + "/NewBlog/public/update_user_experience";
    public static final String DELETE_EXPERIENCE ="http://" + IP + "/NewBlog/public/delete_user_experience/{id}";

    public static final String GET_USER_DATA ="http://" + IP + "/NewBlog/public/getUserInfo/{id}";
    public static final String GET_USER_POSTS ="http://" + IP + "/NewBlog/public/get_user_posts/{id}";

    public static final String COUNT_USER_POST ="http://" + IP + "/NewBlog/public/count_posts/{id}";

    public static final String GET_USER_EXPERIENCES ="http://" + IP + "/NewBlog/public/get_user_experience/{id}";
    public static final String GET_USER_SKILLS ="http://" + IP + "/NewBlog/public/get_user_skill/{id}";
    public static final String UPDATE_USER_INFO ="http://" + IP + "/NewBlog/public/update_user_data";

    public static final String SAVE_RATING_VALUE ="http://" + IP + "/NewBlog/public/save_user_rate";
    public static final String UPDATE_RATING_VALUE ="http://" + IP + "/NewBlog/public/update_user_rate";
    public static final String CHECK_RATING ="http://" + IP + "/NewBlog/public/check_ratting";
    public static final String COUNT_RATING ="http://" + IP + "/NewBlog/public/count_ratting";
    public static final String GET_USER_RATE ="http://" + IP + "/NewBlog/public/get_user_rate/{id}";

    public static final String SAVE_FAVORITE ="http://" + IP + "/NewBlog/public/favoriteSave";
    public static final String DELETE_FAVORITE ="http://" + IP + "/NewBlog/public/favoriteDeletes/{id}";
    public static final String FAVORITE_POSTS ="http://" + IP + "/NewBlog/public/getFavoritedPost/{id}";
    public static final String POST_SOMETHING ="http://" + IP + "/NewBlog/public/post";
    public static final String POST_Edit ="http://" + IP + "/NewBlog/public/EditPost";
    public static final String GET_POSTS_TO_EDIT ="http://" + IP + "/NewBlog/public/getEditPost/{id}";
    public static final String DELETE_POST ="http://" + IP + "/NewBlog/public/delete_post/{id}";

    public static final String REPORT_POST ="http://" + IP + "/NewBlog/public/reportPost";
    public static final String REPORT_USER ="http://" + IP + "/NewBlog/public/reportUser";

    public static final String GET_PASSWORD ="http://" + IP + "/NewBlog/public/getPassword";
    public static final String CHANGE_PASSWORD ="http://" + IP + "/NewBlog/public/changePassword";
    public static final String DELETE_ACCOUNT ="http://" + IP + "/NewBlog/public/deleteAccount/{id}";

}
