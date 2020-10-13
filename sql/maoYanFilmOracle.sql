--------------------------------------------------------
--  文件已创建 - 星期六-九月-26-2020
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table FILMCOMMENT
--------------------------------------------------------

-- 第一次执行，把这几句话注释掉
-- 删库有风险，请慎重考虑
DROP TABLE "C##SCOTTER"."FILMCOMMENT";

CREATE TABLE "C##SCOTTER"."FILMCOMMENT"
(	"ID" numeric(10,0) primary key NOT NULL,
     "FILMNAME" VARCHAR2(255 BYTE)  null ,
     "FILM_COMMENT" VARCHAR2(255 BYTE) default 666 NOT NULL);

Insert into C##SCOTTER.FILMCOMMENT values ('0','蜘蛛侠：英雄远征','只有蜘蛛侠哭着说我不想死。。。哭卿卿～');
Insert into C##SCOTTER.FILMCOMMENT (ID,FILMNAME,FILM_COMMENT) values (1,'蜘蛛侠：英雄远征','拜托，复联3里他们比不是死了，只是没困在某个量子空间，请你们去看蚁人2，复联4里所有人都会回归，而蜘蛛侠2的故事情节发生在复联4后，无语。。。');
Insert into C##SCOTTER.FILMCOMMENT (ID,FILMNAME,FILM_COMMENT) values (2,'蜘蛛侠：英雄远征','看完《复仇者联盟3:无限战争》来搜小蜘蛛的举个手，看看漫威队伍多强大');

--------------------------------------------------------
--  DDL for Index FILMCOMMENT_PK
--------------------------------------------------------




