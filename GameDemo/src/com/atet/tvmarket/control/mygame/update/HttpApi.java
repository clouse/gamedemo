package com.atet.tvmarket.control.mygame.update;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.control.mygame.task.TaskResult;
import com.atet.tvmarket.entity.AutoType;
import com.atet.tvmarket.entity.Group;
import com.atet.tvmarket.model.json.JsonArrayParser;
import com.atet.tvmarket.model.json.JsonObjectParser;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class HttpApi {

	/**
	 * @author wsd
	 * @Description:此方法用于Http方法请求，返回数据中包含了一个列表列表节点对应的字段为data
	 * @date 2012-12-5 下午3:49:48
	 */
	public static <T extends AutoType> TaskResult<Group<T>> getList(String url,
			Class<T> clazz, byte[] param) {
		TaskResult<Group<T>> taskResult = new TaskResult<Group<T>>();
		String m_string;
		try {
			Response response = getHttpPost1(url, param);
			m_string = response.asString();
			if (null == m_string) {
				return null;
			}
			parseList(clazz, m_string, taskResult);
		} catch (Exception e) {
			taskResult.setException(e);
			e.printStackTrace();
		}
		return taskResult;
	}
	
	
	
    /**
     * 
     * @Title: getList   
     * @Description: TODO(此方法用于Http方法请求，返回数据中包含了一个列表列表节点对应的字段为data)   
     * @param: @param firstUrl 优先级最高的服务器地址
     * @param: @param secUrl  优先级第二高的服务器地址
     * @param: @param thirdUrl 优先级第三的服务器地址
     * @param: @param clazz
     * @param: @param param
     * @param: @return      
     * @return: TaskResult<Group<T>>      
     * @throws
     */
	public static <T extends AutoType> TaskResult<Group<T>> getList(String firstUrl,String secUrl,String thirdUrl,
			Class<T> clazz, byte[] param) {
		TaskResult<Group<T>> taskResult = new TaskResult<Group<T>>();
		String m_string;
		try {
			Response response = getHttpPost1(firstUrl, param);
			m_string = response.asString();
			if (null == m_string) {
				return null;
			}
			parseList(clazz, m_string, taskResult);
		} catch (Exception e) {
			taskResult.setException(e);
			e.printStackTrace();
			try {
				Response response = getHttpPost1(secUrl, param);
				m_string = response.asString();
				if (null == m_string) {
					return null;
				}
				parseList(clazz, m_string, taskResult);
			} catch (Exception f){
				taskResult.setException(f);
				f.printStackTrace();
				try {
					Response response = getHttpPost1(thirdUrl, param);
					m_string = response.asString();
					if (null == m_string) {
						return null;
					}
					parseList(clazz, m_string, taskResult);
				} catch (Exception g){
					taskResult.setException(g);
					g.printStackTrace();
				}
			}
		}
		return taskResult;
	}

	/**
	 * @author wsd
	 * @Description:此方法用于Http方法请求，返回单个对象节点中无字段data
	 * @date 2012-12-5 下午3:49:48
	 */
	public static <T extends AutoType> TaskResult<T> getObject(String url,
			Class<T> clazz, byte[] param) {
		TaskResult<T> taskResult = new TaskResult<T>();
		String m_string;
		try {
			Response response = getHttpPost1(url, param); 
			m_string = response.asString();
			if (null == m_string) {
				return null;
			}
			parseObject(clazz, m_string, taskResult);
			
		} catch (Exception e) {
			taskResult.setException(e);
			e.printStackTrace();
		}
		return taskResult;
	}
	
	
	
	/**
	 * 
	 * @author wenfuqiang
	 * @Title: getObject   
	 * @Description: TODO(此方法用于Http方法请求，返回单个对象节点中无字段data)   
	 * @param: @param firstUrl  优先级最高的服务器地址
	 * @param: @param secUrl    优先级第二高的服务器地址
	 * @param: @param thirdUrl  优先级第三的服务器地址
	 * @param: @param clazz
	 * @param: @param param
	 * @param: @return      
	 * @return: TaskResult<T>      
	 * @throws
	 */
	public static <T extends AutoType> TaskResult<T> getObject(String firstUrl,String secUrl,String thirdUrl,
			Class<T> clazz, byte[] param) {
		TaskResult<T> taskResult = new TaskResult<T>();
		String m_string;
		try {
			Response response = getHttpPost1(firstUrl, param); 
			m_string = response.asString();
			if (null == m_string) {
				return null;
			}
			parseObject(clazz, m_string, taskResult);
			
		} catch (Exception e) {
			taskResult.setException(e);
			e.printStackTrace();
			//出异常时从第二优先级的服务器继续获取数据
			try {
				Response response = getHttpPost1(secUrl, param); 
				m_string = response.asString();
				if (null == m_string) {
					return null;
				}
				parseObject(clazz, m_string, taskResult);
			}catch (Exception f){
				taskResult.setException(f);
				f.printStackTrace();
				//出异常时从第三优先级的服务器获取数据
				try {
					Response response = getHttpPost1(thirdUrl, param); 
					m_string = response.asString();
					if (null == m_string) {
						return null;
					}
					parseObject(clazz, m_string, taskResult);
				}catch (Exception g){
					taskResult.setException(g);
					g.printStackTrace();
				}
			}
		}
		return taskResult;
	}

	/**
	 * @author wsd
	 * @Description:向服务器上传一张图片 节点中无字段data
	 * @date 2012-12-5 下午3:49:48
	 */
//	public static <T extends AutoType> TaskResult<T> uploadImage(
//			String imagePath, Class<T> clazz) {
//		TaskResult<T> taskResult = new TaskResult<T>();
//		String m_string;
//		try {
//			m_string = UploadHelper.upload4String(
//					UrlConstant.HTTP_COMMON_PUPLOAD, imagePath);
//			DebugTool.debug(Configuration.DEBUG_TAG, "返回的JOSN字符串：" + m_string);
//			if (null == m_string) {
//				return null;
//			}
//			parseObject(clazz, m_string, taskResult);
//		} catch (Exception e) {
//			taskResult.setException(e);
//		}
//		return taskResult;
//	}

	/**
	 * @author wsd
	 * @Description:发送HttpPost请求，此方法用于XPay系统
	 * @date 2013-5-24 下午3:19:19
	 */
	public static <T extends AutoType> TaskResult<T> httpPostter(String path,
			Class<T> clazz, byte[] param) {
		TaskResult<T> taskResult = new TaskResult<T>();
		String m_string;
		try {
			Response response = getHttpPost1(path, param);
			m_string = response.asString();
			if (null == m_string) {
				return null;
			}
			Gson gson = new Gson();
			taskResult.setData((T) gson.fromJson(m_string, clazz));

			parseCode(taskResult, m_string);
			final int code = taskResult.getCode();
			if (code != 0) {
				throw new ServerLogicalException(code);
			}
		} catch (Exception e) {
			taskResult.setCode(TaskResult.ERROR);
			taskResult.setException(e);
		}
		return taskResult;
	}

	/**
	 * 
	 * @Title: getHttpPost1
	 * @Description: TODO
	 * @param @param path
	 * @param @param param
	 * @param @return
	 * @param @throws MalformedURLException
	 * @param @throws IOException
	 * @param @throws ProtocolException
	 * @return Response
	 * @throws
	 */
	public static <T extends AutoType> Response getHttpPost1(String path,
			byte[] param) throws MalformedURLException, IOException,
			ProtocolException {
		HttpPost post = new HttpPost(path);// 创建 HttpPost对象
		Response result = null;
		HttpResponse httpResponse = null;
		if (param == null) {
			param = new byte[] {};
		}
		ByteArrayEntity entity = new ByteArrayEntity(param);
		post.setEntity(entity); // post以此来设置请求参数
		httpResponse = getHttpClient().getHttpClient()
				.execute(post);

		result = new Response(httpResponse);
		return result;
	}

	/**
	 * 获取网络请求管理器.
	 * 
	 * @return
	 */
	public static HttpClient getHttpClient() {
		HttpClient m_httpClient = null;
		if (m_httpClient == null) {
			m_httpClient = new HttpClient(BaseApplication.getContext());
		}
		return m_httpClient;
	}
	
	/**
	 * 解析code
	 * 
	 * @author fcs
	 * @Description:
	 * @date 2013-5-3 上午9:40:18
	 */
	private static <T extends AutoType> void parseCode(
			TaskResult<T> taskResult, String jsonData) throws IOException {
		// 解析JSON数据，首先要创建一个JsonReader对象
		JsonReader reader = new JsonReader(new StringReader(jsonData));
//		reader.setLenient(true);
		String str ="{code:0}",str1="{code:1100}";
		if(jsonData.equals(str)||jsonData.equals(str1)){
			reader.setLenient(true);
		}
		int code = 0;
		reader.beginObject();
		/*
		 * 返回的json格式{"data":XXX,"errorcode":"XXX","msg":"XXX"}
		 * 通过循环取得data，errorcode,msg,变量的值，放到taskResult对象中
		 */
		while (reader.hasNext()) {
			String tagName = reader.nextName();
			if (tagName.equals("code")) {
				code = reader.nextInt();
				// code==1 请求正确返回，code==2 请求异常
				taskResult.setCode(code);
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
	}

	/**
	 * data 列表解析
	 * 
	 * @author
	 * @Description:
	 * @date 2013-5-22 下午2:52:21
	 * @param taskResult
	 *            TODO
	 */
	private static <T extends AutoType> void parseList(Class<T> clazz,
			String m_string, TaskResult<Group<T>> taskResult) throws Exception {
		JsonObjectParser<T> subParser = new JsonObjectParser<T>(clazz);
		JsonArrayParser arrParser = new JsonArrayParser(subParser);
		arrParser.parseJson(taskResult, m_string);
		final int code = taskResult.getCode();
		// 如果状态码等于1101或1401 没有符合的数据的情况，我们当成成功来处理
		if (code == ServerLogicalException.CODE_GAME_NO_REQUIRED_DATA
				|| code == ServerLogicalException.CODE_GAME_NO_REQUIRED_DATA1) {
			taskResult.setCode(TaskResult.OK);
			// 不直接返回，还是会抛异常的
			return ;
		}
		if (code != 0) {
			throw new ServerLogicalException(code);
		}
	}

	/**
	 * 解析一个对象
	 * 
	 * @author
	 * @Description:
	 * @date 2013-5-22 下午3:00:35
	 * @param taskResult
	 *            TODO
	 */
	private static <T extends AutoType> void parseObject(Class<T> clazz,
			String m_string, TaskResult<T> taskResult) throws Exception {
		Gson gson = new Gson();
		taskResult.setData((T) gson.fromJson(m_string, clazz));
		parseCode(taskResult, m_string);
		final int code = taskResult.getCode();
		// 如果状态码等于1101 没有符合的数据的情况，我们当成成功来处理
//		if (code == ServerLogicalException.CODE_GAME_NO_REQUIRED_DATA) {
//			taskResult.setCode(TaskResult.OK);
//		}
		if (code != 0) {
			throw new ServerLogicalException(code);
		}
	}

	public static String httpPost(String url, byte[] param) {
		String m_string = null;
		try {
			Response response = getHttpPost1(url, param);
			m_string = response.asString();
			if (null == m_string) {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return m_string;
	}
	
	
	public static <T extends AutoType> Response getHttpPost1(String path1,
			String path2, String path3, byte[] param) throws Exception {
		Response result = null;
		try {
			result = getHttpPost1(path1, param);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				result = getHttpPost1(path2, param);
			} catch (Exception e2) {
				e2.printStackTrace();
				result = getHttpPost1(path3, param);
			}

		}

		return result;
	}
}
