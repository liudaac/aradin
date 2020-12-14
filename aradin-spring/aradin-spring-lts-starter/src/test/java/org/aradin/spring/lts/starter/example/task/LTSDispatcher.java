package org.aradin.spring.lts.starter.example.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import com.github.ltsopensource.core.domain.Action;
import com.github.ltsopensource.spring.boot.properties.TaskTrackerProperties;
import com.github.ltsopensource.spring.tasktracker.JobRunnerItem;
import com.github.ltsopensource.spring.tasktracker.LTS;
import com.github.ltsopensource.tasktracker.Result;
import com.github.ltsopensource.tasktracker.runner.JobContext;

@LTS
@ConditionalOnProperty(name="lts.tasktracker.dispatch-runner.enable",havingValue="true")
public class LTSDispatcher {

	private static final Logger LOGGER = LoggerFactory.getLogger(LTSDispatcher.class);
	
	@Autowired(required = false)
    private TaskTrackerProperties properties;
	
	@JobRunnerItem(shardValue="test")
	public Result test(JobContext jobContext) {
		String errMsg = "执行失败";
		try {
			String shardvalue = properties.getDispatchRunner().getShardValue();
			if (jobContext.getJob().getParam(shardvalue).equals("test")) {
				LOGGER.info(">>>>>>>>>>>>>>>lts任务执行成功");
				return new Result(Action.EXECUTE_SUCCESS, "执行成功");
			}
			LOGGER.info(">>>>>>>>>>>>>>>lts任务执行失败");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			errMsg = e.getMessage();
		}
		return new Result(Action.EXECUTE_FAILED, errMsg);
	}
}
