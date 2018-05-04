# luofugong
new
这其中首页右下角有个更新图片按钮     需要搭配FTP服务器才能使用,并且对服务端的存放图片的文件路径有严格的要求,在此作简要说明
this.hostName = "192.168.1.100";
		this.serverPort = 2121;
		this.userName = "sunhq";
		this.password = "1234";
    
    // 定义文件的存取路径(分类存放在不同的文件夹下面)(这里所有的目录到时候根据要求修改,相应的GeyImagePath那个函数里面也要修改)
    public static final String HOME_SHOW = "/FTP/images/";  //家装展示图片路径
    public static final String CORPORATE_HONOR = "/FTP/image/";  //企业荣誉
    public static final String ENGINEERING_CASE = "/FTP/project/";  //工程案列

    String[] serverFolder = new String[]{HOME_SHOW,CORPORATE_HONOR,ENGINEERING_CASE+"parameter1/"
            ,ENGINEERING_CASE+"parameter2/",ENGINEERING_CASE+"parameter3/",ENGINEERING_CASE+"parameter4/"};
            
            
            
    看不懂??  没关系, 理解这个意思就行了
