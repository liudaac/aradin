package org.aradin.spring.lts.starter.example.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.ltsopensource.core.cluster.Node;
import com.github.ltsopensource.core.commons.utils.StringUtils;
import com.github.ltsopensource.core.listener.MasterChangeListener;
import com.github.ltsopensource.spring.boot.annotation.MasterNodeListener;

/**
 * 自动配置
 * @author daliu
 *
 */
@MasterNodeListener
public class MasterNodeChangeListener implements MasterChangeListener{

	private static final Logger LOGGER = LoggerFactory.getLogger(MasterNodeChangeListener.class);
	
	/**
     * @param master   master节点
     * @param isMaster 表示当前节点是不是master节点
     */
	@Override
	public void change(Node master, boolean isMaster) {
		// TODO Auto-generated method stub
		// 一个节点组master节点变化后的处理 , 譬如我多个JobClient， 但是有些事情只想只有一个节点能做。
        if (isMaster) {
            LOGGER.info("我变成了节点组中的master节点了， 恭喜， 我要放大招了");
        } else {
            LOGGER.info(StringUtils.format("master节点变成了{}，不是我，我不能放大招，要猥琐", master));
        }
	}
}
