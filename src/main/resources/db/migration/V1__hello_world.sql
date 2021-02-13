create table channels
(
  id integer
    constraint channels_pk
      primary key autoincrement,
  title text,
  description text,
  itunes_image text,
  language text,
  itunes_category text,
  itunes_explicit text,
  itunes_author text,
  link text,
  itunes_owner text,
  itunes_title text,
  itunes_type text,
  copyright text,
  itunes_new_feed_url text,
  itunes_block text,
  itunes_complete text
);
