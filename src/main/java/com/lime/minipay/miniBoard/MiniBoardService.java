package com.lime.minipay.miniBoard;

import com.lime.minipay.entity.MiniBoard;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@RequiredArgsConstructor
@Service
public class MiniBoardService {
    private final MiniBoardRepository miniBoardRepository;
    private final PlatformTransactionManager transactionManager;

    public void addMiniBoard(final Connection connection, String content, String title) {
        try {
            String sql = "INSERT INTO MINI_BOARD(content, title) VALUES(?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, content);
            preparedStatement.setString(2, title);
            preparedStatement.execute();
        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewBoardWithTxTemplate(Long boardId) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    MiniBoard miniBoard = miniBoardRepository.findByIdWithLock(boardId)
                            .orElseThrow(() -> new RuntimeException());

                    miniBoard.increaseViewCount();
                } catch (RuntimeException ex) {
                    status.setRollbackOnly();
                }
            }
        });
        //다시 알아보기 TransactionCallback
    }

    public void viewBoardWithTxManager(Long boardId) {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            MiniBoard miniBoard = miniBoardRepository.findByIdWithLock(boardId)
                    .orElseThrow(() -> new RuntimeException());

            miniBoard.increaseViewCount();
            transactionManager.commit(status);
        } catch (RuntimeException ex) {
            transactionManager.rollback(status);
            throw ex;
        }
    }
}
