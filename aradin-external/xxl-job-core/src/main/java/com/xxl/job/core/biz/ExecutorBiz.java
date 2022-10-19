package com.xxl.job.core.biz;

import com.xxl.job.core.biz.model.*;

/**
 * Created by xuxueli on 17/3/1.
 */
public interface ExecutorBiz {

    /**
     * beat
     * @return ReturnT
     */
    public ReturnT<String> beat();

    /**
     * idle beat
     *
     * @param idleBeatParam idleBeatParam
     * @return ReturnT
     */
    public ReturnT<String> idleBeat(IdleBeatParam idleBeatParam);

    /**
     * run
     * @param triggerParam triggerParam
     * @return ReturnT
     */
    public ReturnT<String> run(TriggerParam triggerParam);

    /**
     * kill
     * @param killParam killParam
     * @return ReturnT
     */
    public ReturnT<String> kill(KillParam killParam);

    /**
     * log
     * @param logParam logParam
     * @return ReturnT
     */
    public ReturnT<LogResult> log(LogParam logParam);

}
