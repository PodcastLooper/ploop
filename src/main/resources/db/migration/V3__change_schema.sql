drop table channels;
drop table items;

create table channels
(
    id           integer
        constraint channels_pk
            primary key autoincrement,
    title        text,
    description  text,
    language     text,
    image        text,
    category     text,
    explicit     text,
    author       text,
    owner_name   text,
    owner_email  text,
    podcast_type text,
    copyright    text,
    new_feed_url text,
    block        text,
    complete     text,
    link         text
);

INSERT INTO channels (id, title, description, language, image, category, explicit,
                      author, owner_name, owner_email, podcast_type, copyright, new_feed_url, block,
                      complete, link)
VALUES (1, 'Objection', 'A podcast about justice', 'en_US', 'justice.png', 'Justice', false, null, null, null, null,
        null, null, null, null, null);

INSERT INTO channels (id, title, description, language, image, category, explicit,
                      author, owner_name, owner_email, podcast_type, copyright, new_feed_url, block,
                      complete, link)
VALUES (2, 'Space Explorer', 'What is space?', 'en_US', 'space.png', 'Space', false, null, null, null, null, null,
        null, null, null, null);

create table items
(
    id                 integer
        constraint items_pk
            primary key autoincrement,
    channelId          integer,
    title              text,
    enclosure          text,
    guid               text,
    pubDate            text,
    description        text,
    itunes_duration    text,
    itunes_image       text,
    itunes_explicit    text,
    itunes_title       text,
    itunes_episode     text,
    itunes_season      text,
    itunes_episodeType text,
    itunes_block       text
);
