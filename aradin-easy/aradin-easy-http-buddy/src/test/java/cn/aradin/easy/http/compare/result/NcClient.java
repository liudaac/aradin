package cn.aradin.easy.http.compare.result;

import cn.aradin.easy.http.annotation.Controller;
import cn.aradin.easy.http.annotation.RequestBody;
import cn.aradin.easy.http.annotation.RequestHeader;
import cn.aradin.easy.http.annotation.RequestMapping;
import cn.aradin.easy.http.annotation.RequestParam;
import cn.aradin.easy.http.compare.ContentOptDto;
import cn.aradin.easy.http.compare.ContentResultDto;
import cn.aradin.easy.http.compare.DynamicDto;
import cn.aradin.easy.http.compare.GdPoiDto;
import cn.aradin.easy.http.compare.NcResp;
import cn.aradin.easy.http.compare.StatusDto;
import cn.aradin.easy.http.compare.StatusResultDto;
import cn.aradin.easy.http.support.RequestMethod;

@Controller(param = "domain.nc", value = "http://aradin.cn")
public interface NcClient {
	
	@RequestMapping(method = RequestMethod.GET, value = "/poi")
	public NcResp<GdPoiDto> poi(@RequestParam("lonAndLat") String lonAndLat);
	
	@RequestMapping(method = RequestMethod.POST, value = "/add")
	public NcResp<ContentResultDto> add(@RequestHeader(value = "User-U")String userU, @RequestHeader("User-Agent")String userAgent, @RequestBody DynamicDto reqDto);
	
	@RequestMapping(method = RequestMethod.POST, value = "/rec")
	public NcResp<StatusResultDto> rec(@RequestBody StatusDto reqDto);
	
	@RequestMapping(method = RequestMethod.POST, value = "/delete")
	public NcResp<Boolean> delete(@RequestBody ContentOptDto reqDto);
}