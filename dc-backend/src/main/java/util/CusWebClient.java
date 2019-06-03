package util;

import api.estimate.EstimateServlet;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import api.sense.service.svm_predict;
import api.sense.service.svm_train;

import java.io.IOException;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

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

	public static boolean humanTest(int webId,String url) {

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
					String formMd5=webId+encode(formContext);
//					System.out.println("***********************");
//					System.out.println(formContext);
//					System.out.println("***********************");

					//int positiveNum=0;
					if(formContext.indexOf("search")>0||formContext.indexOf("keyword")>0||formContext.indexOf("Search")>0||formContext.indexOf("查询")>0||formContext.indexOf("搜索")>0||formContext.indexOf("关键字")>0){

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
						String formMd5=webId+encode(formContext);
//						System.out.println("***********************");
//						System.out.println(formContext);
//						System.out.println("***********************");
						//int positiveNum=0;
						if(formContext.indexOf("search")>0||formContext.indexOf("keyword")>0||formContext.indexOf("Search")>0||formContext.indexOf("查询")>0||formContext.indexOf("搜索")>0||formContext.indexOf("关键字")>0){
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


	public static int  getVector(String url)throws IOException{


        WebClient webclient = new WebClient(BrowserVersion.CHROME);


			webclient.getOptions().setCssEnabled(false);
			webclient.getOptions().setJavaScriptEnabled(false);
			HtmlPage page = webclient.getPage(url);
			List<DomElement> list1 = page.getElementsByTagName("form");
//            for(int pp=0;pp<list1.size();pp++){
//            	System.out.println(list1.get(pp).asXml());
//            }
			List<HtmlForm> listForm = page.getForms();
			if (listForm.size() == 0) {
				return 1;


			} else if (listForm.size() == 1) {
				List<HtmlElement> listinput = listForm.get(0).getElementsByTagName("input");
				int[] typeNum = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};//button,image,file,text,checkbox,radio,password,hidden,reset,submit,select,textarea,object,label,legend,method,pw,nw
				for (HtmlElement in : listinput) {
					if (in.hasAttribute("type")) {
						if (in.getAttribute("type").equals("button"))
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
				if (typeNum[6] > 0)
					typeNum[6] = 1;
				if (typeNum[9] > 0)
					typeNum[9] = 1;
				List<HtmlElement> listselect = listForm.get(0).getElementsByTagName("select");
				List<HtmlElement> listtextarea = listForm.get(0).getElementsByTagName("textarea");
				List<HtmlElement> listobject = listForm.get(0).getElementsByTagName("object");
				List<HtmlElement> listlabel = listForm.get(0).getElementsByTagName("label");
				List<HtmlElement> listlegend = listForm.get(0).getElementsByTagName("legend");
				typeNum[10] = listselect.size();
				typeNum[11] = listtextarea.size();
				typeNum[12] = listobject.size();
				typeNum[13] = listlabel.size();
				typeNum[14] = listlegend.size();
				if (listForm.get(0).hasAttribute("method")) {
					if (listForm.get(0).getAttribute("method").equals("get"))
						typeNum[15] = 1;
					else if (listForm.get(0).getAttribute("method").equals("post"))
						typeNum[15] = 2;
					else
						typeNum[15] = 3;
				} else
					typeNum[15] = 3;

				//positive
				String formContext = listForm.get(0).asXml();
				//int positiveNum=0;
				if (formContext.indexOf("search") > 0 || formContext.indexOf("搜索") > 0) {
					typeNum[16] = 1;

				}
				//int negativeNum=0;
				if (formContext.indexOf("login") > 0 || formContext.indexOf("账号") > 0 || formContext.indexOf("密码") > 0 || formContext.indexOf("注册") > 0 || formContext.indexOf("username") > 0
						|| formContext.indexOf("password") > 0 || formContext.indexOf("register") > 0 || formContext.indexOf("登录") > 0 || formContext.indexOf("邮箱") > 0 || formContext.indexOf("手机号") > 0) {
					//negativeNum=1;
					typeNum[17] = 1;
				}

				try {
					FileOutputStream fileOutputStream2 = null;
					//String filePath = CusWebClient.class.getResource("")+ "WEB-INF\\trainfile\\train2.txt".substring(1);
					String filePath =CusWebClient.class.getResource("/api/sense/trainfile/train2.txt").getFile().substring(1);
					System.out.println("写入文件路径11111为"+filePath);
					File newfile2 = new File(filePath);
					fileOutputStream2 = new FileOutputStream(newfile2);
					fileOutputStream2.write("1   ".getBytes("UTF-8"));
					for (int j = 0; j < 17; j++) {

						String index=String.valueOf(j+1);
						String each =  index+ ":"+typeNum[j];
						fileOutputStream2.write(each.getBytes("UTF-8"));
						fileOutputStream2.write("  ".getBytes("UTF-8"));
					}
					String finindex = "18:"+typeNum[17] ;
					fileOutputStream2.write(finindex.getBytes("UTF-8"));
					fileOutputStream2.close();





				} catch (Exception e) {
					System.out.println("测试向量写入创建失败！" + e);
				}

				return 2;
			} else if (listForm.size() >= 1) {

				int allform[][] = new int[listForm.size()][18];
				for (int j = 0; j < listForm.size(); j++) {

					List<HtmlElement> listinput = listForm.get(j).getElementsByTagName("input");
					int[] typeNum = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};//button,image,file,text,checkbox,radio,password,hidden,reset,submit,select,textarea,object,label,legend,method,pw,nw
					for (HtmlElement in : listinput) {
						if (in.hasAttribute("type")) {
							if (in.getAttribute("type").equals("button"))
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
					if (typeNum[6] > 0)
						typeNum[6] = 1;
					if (typeNum[9] > 0)
						typeNum[9] = 1;
					List<HtmlElement> listselect = listForm.get(j).getElementsByTagName("select");
					List<HtmlElement> listtextarea = listForm.get(j).getElementsByTagName("textarea");
					List<HtmlElement> listobject = listForm.get(j).getElementsByTagName("object");
					List<HtmlElement> listlabel = listForm.get(j).getElementsByTagName("label");
					List<HtmlElement> listlegend = listForm.get(j).getElementsByTagName("legend");
					typeNum[10] = listselect.size();
					typeNum[11] = listtextarea.size();
					typeNum[12] = listobject.size();
					typeNum[13] = listlabel.size();
					typeNum[14] = listlegend.size();
					if (listForm.get(j).hasAttribute("method")) {
						if (listForm.get(j).getAttribute("method").equals("get"))
							typeNum[15] = 1;
						else if (listForm.get(j).getAttribute("method").equals("post"))
							typeNum[15] = 2;
						else
							typeNum[15] = 3;
					} else
						typeNum[15] = 3;

					String formContext = listForm.get(j).asXml();
					//int positiveNum=0;
					if (formContext.indexOf("search") > 0 || formContext.indexOf("搜索") > 0) {
						typeNum[16] = 1;
						//System.out.println("positive"+formContext.indexOf("search")+"iiiiiiiiiii"+formContext.indexOf("搜索"));

					}
					//int negativeNum=0;
					if (formContext.indexOf("login") > 0 || formContext.indexOf("账号") > 0 || formContext.indexOf("密码") > 0 || formContext.indexOf("注册") > 0 || formContext.indexOf("username") > 0
							|| formContext.indexOf("password") > 0 || formContext.indexOf("register") > 0 || formContext.indexOf("登录") > 0 || formContext.indexOf("邮箱") > 0 || formContext.indexOf("手机号") > 0) {
						//negativeNum=1;
						typeNum[17] = 1;
					}

					//
					for (int pp = 0; pp < 18; pp++) {
						allform[j][pp] = typeNum[pp];
					}


					//
//            	System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
				}
				int flag = 0;
				try {



					FileOutputStream fileOutputStream = null;
					String filePath =CusWebClient.class.getResource("/api/sense/trainfile/train2.txt").getFile().substring(1);
					System.out.println("写入文件路径22222为"+filePath);

					//String filePath = EstimateServlet.projectPath + "WEB-INF\\trainfile\\train2.txt";
					File newfile = new File(filePath);
					fileOutputStream = new FileOutputStream(newfile);

					for (int i = 0; i < allform.length; i++) {
					fileOutputStream.write("1   ".getBytes("UTF-8"));
						for (int j = 0; j < 17; j++) { String index=String.valueOf(j+1);
						String each =  index+ ":"+allform[i][j];
						fileOutputStream.write(each.getBytes("UTF-8"));
						fileOutputStream.write("  ".getBytes("UTF-8"));
						}
						String each2 = allform[i][17] + " ";
						String finindex = "18:"+allform[i][17]  ;
						fileOutputStream.write(finindex.getBytes("UTF-8"));
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

		return 4;
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

	public static boolean judgeurl(int webId,String url){
		//生成form
		WebClient webclient = new WebClient(BrowserVersion.CHROME);
		ArrayList<String> FormMd5s=new ArrayList<>();
		try {
			webclient.getOptions().setCssEnabled(false);
			webclient.getOptions().setJavaScriptEnabled(false);
			HtmlPage page = webclient.getPage(url);
			List<DomElement> list1 = page.getElementsByTagName("form");
//            for(int pp=0;pp<list1.size();pp++){
//            	System.out.println(list1.get(pp).asXml());
//            }
			List<HtmlForm> listForm = page.getForms();
			int length=listForm.size();
			for(int i=0;i<length;i++){
				String formContext=listForm.get(i).asXml();
				String formMd5=webId+encode(formContext);
				FormMd5s.add(formMd5);

			}

		}catch (Exception e){


		}

		//
		boolean flag=false;
		try {
			System.out.println(getVector(url));
			if((getVector(url)!=2)&&(getVector(url)!=3)){
				return flag;}
		}catch (Exception e){

		}

		///////执行
		try {
//			String train1Path= EstimateServlet.projectPath + "WEB-INF\\trainfile\\train1.txt";
//			String modelPath= EstimateServlet.projectPath + "WEB-INF\\trainfile\\model_r.txt";
//			String train2Path= EstimateServlet.projectPath + "WEB-INF\\trainfile\\train2.txt";
//			String outPath= EstimateServlet.projectPath + "WEB-INF\\trainfile\\out_r.txt";
			String train1Path= CusWebClient.class.getResource("/api/sense/trainfile/train1.txt").getFile().substring(1);
			String modelPath= CusWebClient.class.getResource("/api/sense/trainfile/model_r.txt").getFile().substring(1);
			String train2Path= CusWebClient.class.getResource("/api/sense/trainfile/train2.txt").getFile().substring(1);
			String outPath= CusWebClient.class.getResource("/api/sense/trainfile/out_r.txt").getFile().substring(1);


			//String filePath =CusWebClient.class.getResource("/train1.txt").getFile().substring(1);
			String[] arg = {train1Path, // 存放SVM训练模型用的数据的路径
					modelPath}; // 存放SVM通过训练数据训/ //练出来的模型的路径

			String[] parg = {train2Path, // 这个是存放测试数据
					modelPath, // 调用的是训练以后的模型
					outPath}; // 生成的结果的文件的路径
			System.out.println("........SVM运行开始..........");
			// 创建一个训练对象
			svm_train t = new svm_train();
			// 创建一个预测或者分类的对象
			svm_predict p = new svm_predict();
			t.main(arg); // 调用
			p.main(parg); // 调用
			System.out.println("........SVM运行结束..........");
		}catch (Exception e){
			System.out.println("svm执行出错");
		}
		///////执行


		try {

			String filePath= CusWebClient.class.getResource("/api/sense/trainfile/out_r.txt").getFile().substring(1);
			System.out.println("读入文件路径3为"+filePath);
			//String filePath = EstimateServlet.projectPath + "WEB-INF\\trainfile\\out_r.txt";
			File f=new File(filePath);
			BufferedReader br=null;
			try{
				//System.out.println("按照行读取文件内容");
				br=new BufferedReader(new FileReader(f));
				String temp="";
				int tt=0;
				while((temp=br.readLine())!=null){
					System.out.println("结果为"+temp);
					if(temp.equals("1.0")){

						String formmmd5=FormMd5s.get(tt);
						if(!judgeFrom(formmmd5)){
							flag=true;
							String [] p1={"formMd5"};
							String [] p2={formmmd5};

							DBUtil.insert("formsByMd5",p1,p2);

						}

					}
					tt++;
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}




		}
		 catch (Exception e) {
//		    e.printStackTrace();
		   }

		   if(flag==false){
		   	System.out.println("即将进行人工检测");

			   //
			   if(humanTest(webId,url)){
				   flag=true;
				   System.out.println("人工检测 yes");

			   }
			   else {
				   flag=false;
			   }
			   //

		   }



		System.out.println("end");
		return flag;
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







 }
	    }