# proxy-test-mpi

[![Build Status](http://ci.rbkmoney.com/buildStatus/icon?job=rbkmoney_private/proxy-test-mpi/master)](http://ci.rbkmoney.com/job/rbkmoney_private/proxy-test-mpi/master)

Сервис предназначен для эмулирования работы с 3DS MPI


### Developers

- [Anatoly Cherkasov](https://github.com/avcherkasov)
 
Отправка запросов на сервис:

Вовлеченность карты:
```
Method: POST
http(s)//{host}:{port}/mpi/verifyEnrollment
params:
 - pan
 - year
 - month
```

Перенаправление на форму прохождения 3DS

```
Method: POST
http(s)//{host}:{port}/mpi/acs
params:
 - PaReq
 - MD (данные о мерчанте)
 - TermUrl (URL на который будет возвращен результат прохождения 3DS)
```

Проверка прохождения 3DS после получения PaRes:
```
Method: POST
http(s)//{host}:{port}/mpi/validatePaRes
params:
 - pan
 - paRes
```


Конфигурация для docker-compose

```
version: '2'
services:

  proxy_test_mpi:
    image: dr.rbkmoney.com/rbkmoney/proxy-test-mpi:last
    environment:
      - SERVICE_NAME=proxy_test_mpi
    command: |
      -Xms64m -Xmx256m
      -jar /opt/proxy-test-mpi/proxy-test-mpi.jar
      --logging.file=/var/log/proxy-test-mpi/proxy-test-mpi.json
      --proxy-test-mpi.callbackUrl=http://proxy-test-mpi:8080
    working_dir: /opt/proxy-test-mpi
    restart: on-failure:3
    
networks:
  default:
    driver: bridge
    driver_opts:
      com.docker.network.enable_ipv6: "true"
      com.docker.network.bridge.enable_ip_masquerade: "true"
```
