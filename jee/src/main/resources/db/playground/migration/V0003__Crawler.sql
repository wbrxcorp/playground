create table urls(
  id varchar(64) primary key,
  url text,
  canonical_url_id varchar(64) default null,
  content mediumtext,
  created_at datetime
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

create table urls_tags(
  url_id varchar(64) not null,
  tag varchar(64) not null,
  primary key(url_id,tag)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

create table queue (
  url_id varchar(64) primary key,
  url text
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
