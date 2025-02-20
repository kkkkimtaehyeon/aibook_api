# 더미 동화 50개 삽입
delimiter $$
create procedure createStory()
begin
        declare i int default 1;
        while (i <= 50)
            do
                insert into story (base_story, title, created_at, is_public, member_id)
                    values (concat('baseStory', i), concat('title', i), current_timestamp, true, 3);
                set i = i + 1;
end while;
end;
call createStory();
drop procedure createStory;