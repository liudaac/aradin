package cn.aradin.alibaba.sentinel.starter.block;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.util.StringUtil;

import cn.aradin.spring.core.net.http.body.Resp;
import cn.aradin.spring.core.net.http.code.AradinCodedEnum;
import cn.aradin.spring.core.net.http.error.HttpError;


public class AradinHttpBlockExceptionHandler implements BlockExceptionHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
		// TODO Auto-generated method stub
		response.setStatus(429);

        StringBuffer url = request.getRequestURL();
        if ("GET".equals(request.getMethod()) && StringUtil.isNotBlank(request.getQueryString())) {
            url.append("?").append(request.getQueryString());
        }
        PrintWriter out = response.getWriter();
        out.print(Resp.error(HttpError.builder().coded(AradinCodedEnum.TOOFAST).build(), url.toString()));
        out.flush();
        out.close();
	}
	
}
