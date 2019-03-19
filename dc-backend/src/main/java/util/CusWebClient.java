package util;
import java.io.*;
import java.net.MalformedURLException;
import java.util.List;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
public class CusWebClient {
//	static int links=1;
//	static ArrayList<String> checkedHtml=new ArrayList<String>();
//	static ArrayList<String> checkingHtml=new ArrayList<String>();
	private  static MessageDigest md5;

	public static String encode(String str) throws NoSuchAlgorithmException
	{
		md5=MessageDigest.getInstance("MD5");
		MessageDigest md5=MessageDigest.getInstance("MD5");



		byte[]bytes=null;
		try {
			bytes = md5.digest(str.getBytes("utf-8"));
		}catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}
		String s=new String(bytes);
		return  s;


	}

	public static boolean judgeFrom(String md5){
		String[] p1 = {"formMd5"};
		String[][] ans = DBUtil.select("formsByMd5", p1);
		for(int i=0;i<ans.length;i++){
			String formMd5Each=ans[i][0];
		if(formMd5Each.equals(md5)){

			return true;
		}
		}
		return false;
	}

	public static boolean humanTest(String url) {

			WebClient webclient = new WebClient(BrowserVersion.CHROME);

			try {
				webclient.getOptions().setCssEnabled(false);
				webclient.getOptions().setJavaScriptEnabled(false);
				HtmlPage page = webclient.getPage(url);
				List<DomElement> list1 = page.getElementsByTagName("form");
//            for(int pp=0;pp<list1.size();pp++){
//            	System.out.println(list1.get(pp).asXml());
//            }
				List<HtmlForm> listForm = page.getForms();
				if (listForm.size() == 0) {
					return false;
				}
				else if(listForm.size()==1){

					String formContext=listForm.get(0).asXml();
					String formMd5=encode(formContext);
//					System.out.println("***********************");
//					System.out.println(formContext);
//					System.out.println("***********************");

					//int positiveNum=0;
					if(formContext.indexOf("search")>0||formContext.indexOf("搜索")>0||formContext.indexOf("关键字")>0){

						if(judgeFrom(formMd5)){return false;}
						String [] p1={"formMd5"};
						String [] p2={formMd5};

						DBUtil.insert("formsByMd5",p1,p2);



						return true;
					}
					//int negativeNum=0;
					if(formContext.indexOf("login")>0||formContext.indexOf("账号")>0||formContext.indexOf("密码")>0||formContext.indexOf("注册")>0||formContext.indexOf("username")>0
							||formContext.indexOf("password")>0||formContext.indexOf("register")>0||formContext.indexOf("登录")>0||formContext.indexOf("邮箱")>0||formContext.indexOf("手机号")>0)
					{
						//negativeNum=1;
						return  false;
					}
				}

				else if(listForm.size()>1){

					for(int j=0;j<listForm.size();j++) {

						String formContext=listForm.get(j).asXml();
						String formMd5=encode(formContext);
//						System.out.println("***********************");
//						System.out.println(formContext);
//						System.out.println("***********************");
						//int positiveNum=0;
						if(formContext.indexOf("search")>0||formContext.indexOf("搜索")>0||formContext.indexOf("关键字")>0){
							if(judgeFrom(formMd5)){return false;}
							String [] p1={"formMd5"};
							String [] p2={formMd5};

							DBUtil.insert("formsByMd5",p1,p2);

							return true;
						}
						//int negativeNum=0;
						if(formContext.indexOf("login")>0||formContext.indexOf("账号")>0||formContext.indexOf("密码")>0||formContext.indexOf("注册")>0||formContext.indexOf("username")>0
								||formContext.indexOf("password")>0||formContext.indexOf("register")>0||formContext.indexOf("登录")>0||formContext.indexOf("邮箱")>0||formContext.indexOf("手机号")>0)
						{
							//negativeNum=1;
							return  false;
						}
					}



				}



			}catch (Exception e){
				System.out.println(e);
			}


		return  false;
	}


	public static int  getVector(String url){

		try{
        WebClient webclient = new WebClient(BrowserVersion.CHROME);

        try {
            webclient.getOptions().setCssEnabled(false);
            webclient.getOptions().setJavaScriptEnabled(false);
            HtmlPage page=webclient.getPage(url);
            List<DomElement> list1=page.getElementsByTagName("form");
//            for(int pp=0;pp<list1.size();pp++){
//            	System.out.println(list1.get(pp).asXml());
//            }
            List<HtmlForm>listForm=page.getForms();
            if (listForm.size()==0){
            	return 1;


            }

            else if(listForm.size()==1){
            	List<HtmlElement> listinput=listForm.get(0).getElementsByTagName("input");
            	int []typeNum={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};//button,image,file,text,checkbox,radio,password,hidden,reset,submit,select,textarea,object,label,legend,method,pw,nw
            	for(HtmlElement in:listinput){
            		if(in.hasAttribute("type")){
            			if(in.getAttribute("type").equals("button"))
            				typeNum[0]++;
            			else if (in.getAttribute("type").equals("image"))
            				typeNum[1]++;
            			else if (in.getAttribute("type").equals("file"))
            				typeNum[2]++;
            			else if (in.getAttribute("type").equals("text"))
            				typeNum[3]++;
            			else if (in.getAttribute("type").equals("checkbox"))
            				typeNum[4]++;
            			else if (in.getAttribute("type").equals("radio"))
            				typeNum[5]++;
            			else if (in.getAttribute("type").equals("password"))
            				typeNum[6]++;
            			else if (in.getAttribute("type").equals("hidden"))
            				typeNum[7]++;
            			else if (in.getAttribute("type").equals("reset"))
            				typeNum[8]++;
            			else if (in.getAttribute("type").equals("submit"))
            				typeNum[9]++;
            		}

            	}
            	if(typeNum[6]>0)
            		typeNum[6]=1;
            	if(typeNum[9]>0)
            		typeNum[9]=1;
            	List<HtmlElement> listselect=listForm.get(0).getElementsByTagName("select");
            	List<HtmlElement> listtextarea=listForm.get(0).getElementsByTagName("textarea");
            	List<HtmlElement> listobject=listForm.get(0).getElementsByTagName("object");
            	List<HtmlElement> listlabel=listForm.get(0).getElementsByTagName("label");
            	List<HtmlElement> listlegend=listForm.get(0).getElementsByTagName("legend");
            	typeNum[10]=listselect.size();
            	typeNum[11]=listtextarea.size();
            	typeNum[12]=listobject.size();
            	typeNum[13]=listlabel.size();
            	typeNum[14]=listlegend.size();
            	if(listForm.get(0).hasAttribute("method")){
            		if(listForm.get(0).getAttribute("method").equals("get"))
            			typeNum[15]=1;
            		else if(listForm.get(0).getAttribute("method").equals("post"))
            			typeNum[15]=2;
            		else
            			typeNum[15]=3;
            	}
            	else
            		typeNum[15]=3;

            	//positive
            	String formContext=listForm.get(0).asXml();
            	//int positiveNum=0;
            	if(formContext.indexOf("search")>0||formContext.indexOf("搜索")>0){
            		typeNum[16]=1;

            	}
            	//int negativeNum=0;
				if(formContext.indexOf("login")>0||formContext.indexOf("账号")>0||formContext.indexOf("密码")>0||formContext.indexOf("注册")>0||formContext.indexOf("username")>0
						||formContext.indexOf("password")>0||formContext.indexOf("register")>0||formContext.indexOf("登录")>0||formContext.indexOf("邮箱")>0||formContext.indexOf("手机号")>0)
				{
            		//negativeNum=1;
            		typeNum[17]=1;
            	}

            	try {
    			 	FileOutputStream fileOutputStream = null;
    	            File newfile = new File("D:/crawler/params1.txt");
    	            fileOutputStream = new FileOutputStream(newfile);

    	            	for(int j=0;j<17;j++){
    	            		String each=typeNum[j]+" ";
    	            		fileOutputStream.write(each.getBytes("UTF-8"));
    	            		fileOutputStream.write(",".getBytes("UTF-8"));
    	            	}
    	            	String each2=typeNum[17]+" ";
                		fileOutputStream.write(each2.getBytes("UTF-8"));



//    	            fileOutputStream.write(content.getBytes("UTF-8"));
    	            fileOutputStream.close();
    	        } catch (Exception e) {
    	            System.out.println("java文件单form写入创建失败！" + e);
    	        }

            	return 2;
            }
            else if (listForm.size()>=1){

            	int allform[][]= new int[listForm.size()][18];
            for(int j=0;j<listForm.size();j++){

            	List<HtmlElement> listinput=listForm.get(j).getElementsByTagName("input");
            	int []typeNum={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};//button,image,file,text,checkbox,radio,password,hidden,reset,submit,select,textarea,object,label,legend,method,pw,nw
            	for(HtmlElement in:listinput){
            		if(in.hasAttribute("type")){
            			if(in.getAttribute("type").equals("button"))
            				typeNum[0]++;
            			else if (in.getAttribute("type").equals("image"))
            				typeNum[1]++;
            			else if (in.getAttribute("type").equals("file"))
            				typeNum[2]++;
            			else if (in.getAttribute("type").equals("text"))
            				typeNum[3]++;
            			else if (in.getAttribute("type").equals("checkbox"))
            				typeNum[4]++;
            			else if (in.getAttribute("type").equals("radio"))
            				typeNum[5]++;
            			else if (in.getAttribute("type").equals("password"))
            				typeNum[6]++;
            			else if (in.getAttribute("type").equals("hidden"))
            				typeNum[7]++;
            			else if (in.getAttribute("type").equals("reset"))
            				typeNum[8]++;
            			else if (in.getAttribute("type").equals("submit"))
            				typeNum[9]++;
            		}

            	}
            	if(typeNum[6]>0)
            		typeNum[6]=1;
            	if(typeNum[9]>0)
            		typeNum[9]=1;
            	List<HtmlElement> listselect=listForm.get(j).getElementsByTagName("select");
            	List<HtmlElement> listtextarea=listForm.get(j).getElementsByTagName("textarea");
            	List<HtmlElement> listobject=listForm.get(j).getElementsByTagName("object");
            	List<HtmlElement> listlabel=listForm.get(j).getElementsByTagName("label");
            	List<HtmlElement> listlegend=listForm.get(j).getElementsByTagName("legend");
            	typeNum[10]=listselect.size();
            	typeNum[11]=listtextarea.size();
            	typeNum[12]=listobject.size();
            	typeNum[13]=listlabel.size();
            	typeNum[14]=listlegend.size();
            	if(listForm.get(j).hasAttribute("method")){
            		if(listForm.get(j).getAttribute("method").equals("get"))
            			typeNum[15]=1;
            		else if(listForm.get(j).getAttribute("method").equals("post"))
            			typeNum[15]=2;
            		else
            			typeNum[15]=3;
            	}
            	else
            		typeNum[15]=3;

            	String formContext=listForm.get(j).asXml();
            	//int positiveNum=0;
				if(formContext.indexOf("search")>0||formContext.indexOf("搜索")>0){
					typeNum[16]=1;
					//System.out.println("positive"+formContext.indexOf("search")+"iiiiiiiiiii"+formContext.indexOf("搜索"));

				}
				//int negativeNum=0;
				if(formContext.indexOf("login")>0||formContext.indexOf("账号")>0||formContext.indexOf("密码")>0||formContext.indexOf("注册")>0||formContext.indexOf("username")>0
						||formContext.indexOf("password")>0||formContext.indexOf("register")>0||formContext.indexOf("登录")>0||formContext.indexOf("邮箱")>0||formContext.indexOf("手机号")>0)
				{
					//negativeNum=1;
					typeNum[17]=1;
				}
//            	System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//            	System.out.println("��"+(j+1)+"��form����Ϊ��");
//            	System.out.print("[");
//            	for(int i=0;i<typeNum.length;i++){
//            		System.out.print(typeNum[i]);
//            		if(i!=typeNum.length-1){
//            			System.out.print(",");
//            		}
//            	}
//            	System.out.println("]");

            	//
            	for(int pp=0;pp<18;pp++){
            		allform[j][pp]=typeNum[pp];
            	}


            	//
//            	System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            }
           int flag = 0;
    		try {
				FileOutputStream fileOutputStream = null;
				File newfile = new File("D:/crawler/params1.txt");
				fileOutputStream = new FileOutputStream(newfile);

    	            for(int i=0;i<allform.length;i++){
    	            	for(int j=0;j<17;j++){
    	            		String each=allform[i][j]+" ";
    	            		fileOutputStream.write(each.getBytes("UTF-8"));
    	            		fileOutputStream.write(",".getBytes("UTF-8"));
    	            	}
    	            	String each2=allform[i][17]+" ";
                		fileOutputStream.write(each2.getBytes("UTF-8"));

                		fileOutputStream.write("\r\n".getBytes("UTF-8"));
    	            }
//    	            fileOutputStream.write(content.getBytes("UTF-8"));
    	            fileOutputStream.close();
    	            flag = 3;
    	        } catch (Exception e) {
    	            System.out.println("文件创建失败" + e);
    	        }



            return flag;
            }

//            HtmlListItem item = (HtmlListItem) page.getByXPath("//div[@id='sabv'][1]/div/ul/li").get(0);// xpath�ķ�ʽ��ȡ
//            System.out.println(item.asXml());
        } catch (FailingHttpStatusCodeException e) {


        	try{
        		File f = new File("D:/crawler/params1.txt"); // ����Ҫɾ�����ļ�λ��
        		if(f.exists())
        		f.delete();
        		}
        		catch (Exception e2) {
        			// TODO: handle exception
        			System.out.println("删除文件错误");
        		}

            // TODO Auto-generated catch block
            e.printStackTrace();
            webclient.close(); // �رտͻ��ˣ��ͷ��ڴ�
            return 0;
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
        	try{
        		File f = new File("D:/crawler/params1.txt"); // ����Ҫɾ�����ļ�λ��
        		if(f.exists())
        		f.delete();
        		}
        		catch (Exception e2) {
        			// TODO: handle exception
        			System.out.println("删除文件错误");
        		}
            e.printStackTrace();

            webclient.close(); // �رտͻ��ˣ��ͷ��ڴ�
            return 0;
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	try{
        		File f = new File("D:/crawler/params1.txt"); // ����Ҫɾ�����ļ�λ��
        		if(f.exists())
        		f.delete();
        		}
        		catch (Exception e2) {
        			// TODO: handle exception
        			System.out.println("删除文件错误");
        		}
            e.printStackTrace();
            webclient.close(); // �رտͻ��ˣ��ͷ��ڴ�
            return 0;
        } }
		catch (Exception e) {
			// TODO: handle exception
			try{
        		File f = new File("D:/crawler/params1.txt"); // ����Ҫɾ�����ļ�λ��
        		if(f.exists())
        		f.delete();
        		}
        		catch (Exception e2) {
        			// TODO: handle exception
        			System.out.println("删除文件错误");
        		}
			return 0;
		}

		try{
		File f = new File("D:/crawler/params1.txt"); // ����Ҫɾ�����ļ�λ��
		if(f.exists())
		f.delete();
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("删除文件错误");
		}
        return 6;

    	}

	public  static boolean writetxt(int [][] vectors){

		boolean flag = false;
		try {
			 	FileOutputStream fileOutputStream = null;
	            File newfile = new File("D:/crawler/params1.txt");
	            fileOutputStream = new FileOutputStream(newfile);

	            for(int i=0;i<vectors.length;i++){
	            	for(int j=0;j<15;j++){
	            		String each=vectors[i][j]+" ";
	            		fileOutputStream.write(each.getBytes("UTF-8"));
	            		fileOutputStream.write(",".getBytes("UTF-8"));
	            	}
	            	String each2=vectors[i][17]+" ";
            		fileOutputStream.write(each2.getBytes("UTF-8"));

            		fileOutputStream.write("\r\n".getBytes("UTF-8"));
	            }
//	            fileOutputStream.write(content.getBytes("UTF-8"));
	            fileOutputStream.close();
	            flag = true;
	        } catch (Exception e) {
	        }

		return flag;

	}
	public static void method1(String filespace,String urlline) {
		FileWriter fw = null;
		try {
			//如果文件存在，则追加内容；如果文件不存在，则创建文件
		File f=new File(filespace);
		fw = new FileWriter(f, true);
		} catch (IOException e) {
		e.printStackTrace();
		}
		PrintWriter pw = new PrintWriter(fw);
		pw.println(urlline);
		pw.flush();
		try {
		fw.flush();
		pw.close();
		fw.close();
		} catch (IOException e) {
		e.printStackTrace();
		}
		}

	public static boolean judgeurl(String url){
		boolean flag=false;
		System.out.println(getVector(url));
		if((getVector(url)!=2)&&(getVector(url)!=3)){
			return flag;}



		try {
			//can't use absolute address in project
			//must to change!
		    String filespace="D:/crawler/params1.txt";
		    System.out.println("start;");
		    String[] args1 = new String[] { "python", "D:\\crawler\\svm3.py",filespace};
		    Process pr=Runtime.getRuntime().exec(args1);
		    BufferedReader in = new BufferedReader(new InputStreamReader(
		      pr.getInputStream()));
		    String line;
		    while ((line = in.readLine()) != null) {
		     if(line.equals("1")){
		    	 flag=true;
		     }
		    }
		    in.close();
		    pr.waitFor();

		    System.out.println("end");
		   }
		 catch (Exception e) {
//		    e.printStackTrace();
		   }

		   if(flag==false){

			   //
			   if(humanTest(url)){
				   flag=true;

			   }
			   else {
				   flag=false;
			   }
			   //

		   }




		return flag;
	}
//public  static void allpage(ArrayList<String> url){
//		if(links!=0){
//			System.out.println("link number:"+links+" "+checkingHtml.size());
//			ArrayList<HtmlPage> html = null;
//
//
//		}
//
//
//	}

	public static void panduan(String url){
		if (judgeurl(url)) {
			CusWebClient.method1("D:\\crawler\\ParamText.txt",url);
		}
	}
	public static void main(String[] args) {
	    	//getVector("https://www.douban.com/");
		//method1("C:\\Users\\27148\\Desktop\\pp4.txt","������");
	 // judgeurl("https://www.douban.com/group/explore?tag=ֱ��");
//		if(judgeurl("https://www.douban.com/"))
//			System.out.println(">>>>");;
		//getVector("https://www.douban.com/");
		//method1("C:\\Users\\27148\\Desktop\\pp5.txt","url");
			//getVector("https://www.douban.com/group/shafake");
		//////////////////







		////////////////////


	//panduan("https://www.douban.com/gallery/topic/53336/?from=hot_topic_anony_sns");
//
//		if(judgeurl("")){
//
//			System.out.println("正确");
//		};
		try{
			String pp = encode("poiu");
			String pp1 = encode("poiu");
			String pp2 = encode("poiu");
			System.out.println(pp);
			System.out.println(pp1);System.out.println(pp2);

		}catch (Exception e){

			System.out.println("error");
		}






 }
	    }