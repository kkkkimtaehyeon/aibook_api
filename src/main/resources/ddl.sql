CREATE TABLE `member`
(
    `member_id`     BIGINT       NOT NULL AUTO_INCREMENT,
    `email`         VARCHAR(320) NOT NULL,
    `nick_name`     VARCHAR(20)  NOT NULL,
    `birth_date`    DATE         NOT NULL,
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

delete
from oauth_member;

CREATE TABLE story
(
    story_id   BIGINT NOT NULL AUTO_INCREMENT,
    base_story TEXT   NOT NULL,
    title      VARCHAR(100),
    created_at DATETIME,
    is_public  TINYINT(1),
    member_id  BIGINT NOT NULL,
    PRIMARY KEY (story_id),
    FOREIGN KEY (member_id) REFERENCES member (member_id)
);

CREATE TABLE story_page
(
    story_page_id BIGINT       NOT NULL AUTO_INCREMENT,
    page_number   TINYINT      NOT NULL,
    content       VARCHAR(300) NOT NULL,
    story_id      BIGINT       NOT NULL,
    PRIMARY KEY (story_page_id),
    FOREIGN KEY (story_id) REFERENCES story (story_id)

)