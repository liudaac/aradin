package org.aradin.spring.lts.starter.example.task;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import com.github.ltsopensource.core.domain.Action;
import com.github.ltsopensource.spring.boot.annotation.JobRunner4TaskTracker;
import com.github.ltsopensource.tasktracker.Result;
import com.github.ltsopensource.tasktracker.runner.JobContext;
import com.github.ltsopensource.tasktracker.runner.JobRunner;

/**
 * JobRunner两种模式
 * 1、@JobRunner4TaskTracker 全局只能存在一个，作为单一jobrunner模式
 * 2、@LTS 可以声明多个，实现job执行类，每个方法加JobRunnerItem注解，使用shardField区分，即执行方法注解中包含默认的shardField参数（默认取值taskId）,将每个方法实现构造为对应的JobRunner，并按照分配对应的shardField值生成映射关系，分发任务时直接分配
 * @author daliu
 *
 */
@JobRunner4TaskTracker
@ConditionalOnProperty(name="lts.tasktracker.dispatch-runner.enable",havingValue="false")
public class JobRunnerDispatcher implements JobRunner {

	@Override
	public Result run(JobContext jobContext) throws Throwable {
		// TODO Auto-generated method stub
		try {
//			Job job = jobContext.getJob();
//			String type = job.getParam("type");
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(">>>>>>>>>>>>>>>lts任务执行失败");
			e.printStackTrace();
			return new Result(Action.EXECUTE_FAILED, e.getMessage());
		}
		System.out.println(">>>>>>>>>>>>>>>lts任务执行成功");
		return new Result(Action.EXECUTE_SUCCESS, "执行成功");
	}

}
