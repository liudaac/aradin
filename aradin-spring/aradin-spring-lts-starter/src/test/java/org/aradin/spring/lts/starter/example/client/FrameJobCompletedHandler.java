package org.aradin.spring.lts.starter.example.client;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import com.github.ltsopensource.core.domain.JobResult;
import com.github.ltsopensource.jobclient.support.JobCompletedHandler;
import com.github.ltsopensource.spring.boot.annotation.JobCompletedHandler4JobClient;
@JobCompletedHandler4JobClient
public class FrameJobCompletedHandler implements JobCompletedHandler {

	@Override
	public void onComplete(List<JobResult> jobResults) {
		// TODO Auto-generated method stub
		// 任务执行反馈结果处理
        if (CollectionUtils.isNotEmpty(jobResults)) {
            for (JobResult jobResult : jobResults) {
            	if (!jobResult.isSuccess()) {
            		
				}
            }
        }
	}
}
