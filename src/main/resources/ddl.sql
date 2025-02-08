CREATE TABLE `member`
(
    `member_id`     BIGINT       NOT NULL AUTO_INCREMENT,
    `email`         VARCHAR(320) NOT NULL,
    `nick_name`     VARCHAR(20)  NOT NULL,
    `birth_date`     DATE         NOT NULL,
    `created_at`    DATETIME     NOT NULL,
    `role`          VARCHAR(20)  NOT NULL,
    oauth_member_id BIGINT       NULL,
    primary key (member_id),
    FOREIGN KEY (oauth_member_id) references oauth_member (oauth_member_id)
);
drop table member;
CREATE TABLE oauth_member
(
    oauth_member_id    BIGINT       NOT NULL AUTO_INCREMENT,
    provider           VARCHAR(100) NOT NULL,
    provider_member_id BIGINT       NOT NULL,
    primary key (oauth_member_id)
);

delete from oauth_member;