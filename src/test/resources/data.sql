
insert into ANALYSIS_RESULT(id, version, change_time, result_id) values (1000, 0, systimestamp, 'result1');

insert into HOT_TOPIC (id, version, change_time, analysis_result_id, keyword, occurence) values (100, 0, systimestamp, 1000, 'keyword1', 10);

insert into FEED_ITEM (id, version, change_time, hot_topic_id, title, link) values (100, 0, systimestamp, 100,  'title1', 'link1');
insert into FEED_ITEM (id, version, change_time, hot_topic_id, title, link) values (110, 0, systimestamp, 100,  'title2', 'link2');

insert into HOT_TOPIC (id, version, change_time, analysis_result_id, keyword, occurence) values (200, 0, systimestamp, 1000, 'keyword2', 20);

insert into FEED_ITEM (id, version, change_time, hot_topic_id, title, link) values (200, 0, systimestamp, 200,  'title1', 'link1');
insert into FEED_ITEM (id, version, change_time, hot_topic_id, title, link) values (220, 0, systimestamp, 200,  'title2', 'link2');
