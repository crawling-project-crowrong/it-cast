#!/bin/bash

DEPLOY_BASE_PATH=/home/ubuntu/itcast
LOG_PATH=/home/ubuntu/deploy.log
ERR_LOG_PATH=/home/ubuntu/deploy_err.log

declare -A MODULES
MODULES=(
  ["admin"]="admin"
  ["b2c"]="b2c"
  ["schedule"]="schedule"
)

echo ">>> 배포 시작" >> $LOG_PATH

for MODULE in "${!MODULES[@]}"
do
  MODULE_NAME=${MODULES[$MODULE]}
  MODULE_DEPLOY_PATH=$DEPLOY_BASE_PATH/$MODULE_NAME

  echo ">>> $MODULE_NAME 배포 시작" >> $LOG_PATH

  # 배포 경로 생성
  mkdir -p $MODULE_DEPLOY_PATH
  echo ">>> $MODULE_NAME 디렉토리 생성 완료" >> $LOG_PATH

  # 빌드된 JAR 파일 찾기
  BUILD_JAR=$(ls /home/ubuntu/itcast/$MODULE_NAME/build/libs/*.jar)
  JAR_NAME=$(basename $BUILD_JAR)
  echo ">>> $MODULE_NAME build 파일명: $JAR_NAME" >> $LOG_PATH

  # JAR 파일 복사
  echo ">>> $MODULE_NAME build 파일 복사" >> $LOG_PATH
  cp $BUILD_JAR $MODULE_DEPLOY_PATH 2>> $ERR_LOG_PATH

  # 실행 중인 애플리케이션 종료
  echo ">>> $MODULE_NAME 실행 중인 애플리케이션 종료" >> $LOG_PATH
  PID=$(ps -ef | grep $MODULE_NAME | grep java | awk '{print $2}')
  if [ -n "$PID" ]; then
    kill -15 $PID
    echo ">>> $MODULE_NAME 기존 프로세스 종료 완료 (PID: $PID)" >> $LOG_PATH
  else
    echo ">>> $MODULE_NAME 실행 중인 프로세스 없음" >> $LOG_PATH
  fi

  # 새 JAR 실행
  DEPLOY_JAR=$MODULE_DEPLOY_PATH/$JAR_NAME
  echo ">>> $MODULE_NAME 새 JAR 실행: $DEPLOY_JAR" >> $LOG_PATH
  nohup java -jar -Dspring.profiles.active=prod $DEPLOY_JAR >> $LOG_PATH 2>> $ERR_LOG_PATH &

  echo ">>> $MODULE_NAME 배포 완료" >> $LOG_PATH
done

echo ">>> 배포 완료" >> $LOG_PATH
