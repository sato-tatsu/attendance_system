package model.analysis;

import java.util.List;

import javax.persistence.EntityManager;

import model.Approve;
import util.DBUtil;

public class ApproveAnalysis {
    public static Integer getNextApproveState(Integer now_approve, Boolean admin_flag)
    {
        EntityManager em = DBUtil.createEntityManager();

        Integer next_state = 0;
        List<Approve> approve_list = em.createNamedQuery("getAllApprove", Approve.class).getResultList();

        // 承認ステータスマスターから遷移先を照会
        for (Approve approve : approve_list)
        {
            if (now_approve == approve.getId())
            {
                if (admin_flag == true)
                {// 管理者による承認ステータス遷移
                    next_state = approve.getAdmin_next_state();
                    break;
                }
                else
                {// 一般による承認ステータス遷移
                    next_state = approve.getNormal_next_state();
                    break;
                }
            }
        }
        em.close();
        return next_state;
    }

}
