create table channels
(
    id                  integer
        constraint channels_pk
            primary key autoincrement,
    title               text,
    description         text,
    itunes_image        text,
    language            text,
    itunes_category     text,
    itunes_explicit     text,
    itunes_author       text,
    itunes_owner        text,
    itunes_title        text,
    itunes_type         text,
    copyright           text,
    itunes_new_feed_url text,
    itunes_block        text,
    itunes_complete     text
);

INSERT INTO channels (id, title, description, itunes_image, language, itunes_category, itunes_explicit, itunes_author,
                      itunes_owner, itunes_title, itunes_type, copyright, itunes_new_feed_url, itunes_block,
                      itunes_complete)
VALUES (1, 'Objection', 'A podcast about justice', 'justice.png', 'en_US', null, null, null, null, null, null,
        null, null, null, null);

INSERT INTO channels (id, title, description, itunes_image, language, itunes_category, itunes_explicit, itunes_author,
                      itunes_owner, itunes_title, itunes_type, copyright, itunes_new_feed_url, itunes_block,
                      itunes_complete)
VALUES (2, 'Space Explorer', 'What is space?', 'space.png', 'en_US', null, null, null, null, null, null, null,
        null, null, null);

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
