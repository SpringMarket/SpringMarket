package product.config.rds;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Map;

import static java.util.stream.Collectors.toList;

@Slf4j
public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {

    private ReplicationRoutingCircularList<String> replicationRoutingDataSourceNameList;

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);

        replicationRoutingDataSourceNameList = new ReplicationRoutingCircularList<>(
                targetDataSources.keySet()
                        .stream()
                        .map(Object::toString)
                        .filter(s -> s.contains("replica"))
                        .collect(toList()));
    }

    @Override
    protected Object determineCurrentLookupKey() {
        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        if (isReadOnly) {
            String replicaName = replicationRoutingDataSourceNameList.getOne();
            log.info("Replica DB name : {}",replicaName); // 테스트에 찍어보기 위한 로그, 운영시 제거
            return replicaName;
        }
        log.info("Main DB name : {}","main"); // 테스트에 찍어보기 위한 로그, 운영시 제거
        return "main";
    }
}