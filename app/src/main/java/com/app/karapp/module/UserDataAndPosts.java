package com.app.karapp.module;

public class UserDataAndPosts {

    //user
    String name;
    String last_name;
    int phone;
    String u_image;
    String job;
    int u_province;
    String password;

    //Post
    int id;
    int user_id;
    String title;
    String text;
    String image;
    String adress;
    String category;
    int p_count;

    //Category
    String Cname;

    //province
    String pName;

    //skill
    String skill;
    String level;
    int s_id;

    //experiences
    String extitle;
    int duration;
    int ex_id;

    //rating
    int r_id;
    float value;
    int r_count;

    //favorite

    int f_id;
    int status;

    public String getPassword () {
        return password;
    }

    public void setPassword (String password) {
        this.password = password;
    }

    public int getS_id () {
        return s_id;
    }

    public void setS_id (int s_id) {
        this.s_id = s_id;
    }

    public int getEx_id () {
        return ex_id;
    }

    public void setEx_id (int ex_id) {
        this.ex_id = ex_id;
    }

    public String getSkill () {
        return skill;
    }

    public void setSkill (String skill) {
        this.skill = skill;
    }

    public String getLevel () {
        return level;
    }

    public void setLevel (String level) {
        this.level = level;
    }

    public String getExtitle () {
        return extitle;
    }

    public void setExtitle (String extitle) {
        this.extitle = extitle;
    }

    public int getDuration () {
        return duration;
    }

    public void setDuration (int duration) {
        this.duration = duration;
    }

    public String getCategory () {
        return category;
    }

    public void setCategory (String category) {
        this.category = category;
    }

    public String getpName () {
        return pName;
    }

    public void setpName (String pName) {
        this.pName = pName;
    }

    public int getUser_id () {
        return user_id;
    }

    public void setUser_id (int user_id) {
        this.user_id = user_id;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getLast_name () {
        return last_name;
    }

    public void setLast_name (String last_name) {
        this.last_name = last_name;
    }

    public int getPhone () {
        return phone;
    }

    public void setPhone (int phone) {
        this.phone = phone;
    }

    public String getU_image () {
        return u_image;
    }

    public void setU_image (String u_image) {
        this.u_image = u_image;
    }

    public String getJob () {
        return job;
    }

    public void setJob (String job) {
        this.job = job;
    }

    public int getU_province () {
        return u_province;
    }

    public void setU_province (int u_province) {
        this.u_province = u_province;
    }

    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public String getTitle () {
        return title;
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public String getText () {
        return text;
    }

    public void setText (String text) {
        this.text = text;
    }

    public String getImage () {
        return image;
    }

    public void setImage (String image) {
        this.image = image;
    }

    public String getAdress () {
        return adress;
    }

    public void setAdress (String adress) {
        this.adress = adress;
    }

    public String getCname () {
        return Cname;
    }

    public void setCname (String cname) {
        Cname = cname;
    }

    public int getR_id () {
        return r_id;
    }

    public void setR_id (int r_id) {
        this.r_id = r_id;
    }

    public float getValue () {
        return value;
    }

    public void setValue (float value) {
        this.value = value;
    }

    public int getR_count () {
        return r_count;
    }

    public void setR_count (int r_count) {
        this.r_count = r_count;
    }

    public int getP_count () {
        return p_count;
    }

    public void setP_count (int p_count) {
        this.p_count = p_count;
    }

    public int getF_id () {
        return f_id;
    }

    public void setF_id (int f_id) {
        this.f_id = f_id;
    }

    public int getStatus () {
        return status;
    }

    public void setStatus (int status) {
        this.status = status;
    }
}
