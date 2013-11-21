# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table category (
  id                        bigint auto_increment not null,
  title                     varchar(255),
  description               varchar(255),
  parent_id                 bigint,
  constraint pk_category primary key (id))
;

create table comment (
  id                        bigint auto_increment not null,
  video_id                  bigint not null,
  text                      varchar(255),
  author_id                 bigint,
  registered                datetime,
  constraint pk_comment primary key (id))
;

create table series (
  id                        bigint auto_increment not null,
  author_id                 bigint,
  title                     varchar(255),
  description               varchar(255),
  tags                      varchar(255),
  views                     integer,
  votes                     integer,
  constraint pk_series primary key (id))
;

create table session (
  id                        bigint auto_increment not null,
  connected_at              datetime,
  cookie                    varchar(255),
  constraint pk_session primary key (id))
;

create table user (
  id                        bigint auto_increment not null,
  first_name                varchar(255),
  last_name                 varchar(255),
  session_id                bigint,
  age                       integer,
  is_admin                  tinyint(1) default 0,
  mail                      varchar(255),
  password                  varchar(255),
  registered                datetime,
  constraint pk_user primary key (id))
;

create table video (
  id                        bigint auto_increment not null,
  author_id                 bigint,
  url                       varchar(255),
  title                     varchar(255),
  description               varchar(255),
  tags                      varchar(255),
  category_id               bigint,
  views                     integer,
  votes                     integer,
  constraint pk_video primary key (id))
;


create table user_fav_videos (
  user_id                        bigint not null,
  video_id                       bigint not null,
  constraint pk_user_fav_videos primary key (user_id, video_id))
;

create table user_fav_series (
  user_id                        bigint not null,
  series_id                      bigint not null,
  constraint pk_user_fav_series primary key (user_id, series_id))
;

create table video_series (
  video_id                       bigint not null,
  series_id                      bigint not null,
  constraint pk_video_series primary key (video_id, series_id))
;
alter table category add constraint fk_category_parent_1 foreign key (parent_id) references category (id) on delete restrict on update restrict;
create index ix_category_parent_1 on category (parent_id);
alter table comment add constraint fk_comment_video_2 foreign key (video_id) references video (id) on delete restrict on update restrict;
create index ix_comment_video_2 on comment (video_id);
alter table comment add constraint fk_comment_author_3 foreign key (author_id) references user (id) on delete restrict on update restrict;
create index ix_comment_author_3 on comment (author_id);
alter table series add constraint fk_series_author_4 foreign key (author_id) references user (id) on delete restrict on update restrict;
create index ix_series_author_4 on series (author_id);
alter table user add constraint fk_user_session_5 foreign key (session_id) references session (id) on delete restrict on update restrict;
create index ix_user_session_5 on user (session_id);
alter table video add constraint fk_video_author_6 foreign key (author_id) references user (id) on delete restrict on update restrict;
create index ix_video_author_6 on video (author_id);
alter table video add constraint fk_video_category_7 foreign key (category_id) references category (id) on delete restrict on update restrict;
create index ix_video_category_7 on video (category_id);



alter table user_fav_videos add constraint fk_user_fav_videos_user_01 foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table user_fav_videos add constraint fk_user_fav_videos_video_02 foreign key (video_id) references video (id) on delete restrict on update restrict;

alter table user_fav_series add constraint fk_user_fav_series_user_01 foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table user_fav_series add constraint fk_user_fav_series_series_02 foreign key (series_id) references series (id) on delete restrict on update restrict;

alter table video_series add constraint fk_video_series_video_01 foreign key (video_id) references video (id) on delete restrict on update restrict;

alter table video_series add constraint fk_video_series_series_02 foreign key (series_id) references series (id) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table category;

drop table comment;

drop table series;

drop table video_series;

drop table session;

drop table user;

drop table user_fav_videos;

drop table user_fav_series;

drop table video;

SET FOREIGN_KEY_CHECKS=1;

