create or replace procedure upvote_post(IN in_userId character varying, IN in_postId character varying, OUT message character varying)
    language plpgsql
as
'
DECLARE
    v_vote user_vote_post%rowtype;
BEGIN
    SELECT *
    FROM user_vote_post
    WHERE userId = in_userId
      AND postId = in_postId
    LIMIT 1
    INTO v_vote;

    IF NOT FOUND THEN
        message := ''upvoted your post'';
        INSERT INTO user_vote_post (id, userId, postId, type)
        VALUES (nextval(''user_vote_post_sequence''), in_userId, in_postId, 1);
        INSERT INTO post_votes (postId, upvotes, downvotes)
        VALUES (in_postId, 1, 0)
        ON CONFLICT (postId)
            DO UPDATE
            SET upvotes = post_votes.upvotes + 1
            WHERE post_votes.postId = in_postId;
    ELSE
        IF v_vote.type = 1 THEN
            message := ''removed your upvote on the post'';
            DELETE
            FROM user_vote_post
            WHERE userId = in_userId
              AND postId = in_postId;
            UPDATE post_votes
            SET upvotes = upvotes - 1
            WHERE postId = in_postId;
        ELSE
            message := ''removed your downvote on the post, and upvoted it instead'';
            UPDATE user_vote_post
            SET type = 1
            WHERE userId = in_userId
              AND postId = in_postId;
            UPDATE post_votes
            SET upvotes   = upvotes + 1,
                downvotes = downvotes - 1
            WHERE postId = in_postId;
        END IF;
    END IF;
END;
';

alter procedure upvote_post(varchar, varchar, out varchar) owner to maria;

create or replace procedure downvote_post(IN in_userId character varying, IN in_postId character varying, OUT message character varying)
    language plpgsql
as
'
    DECLARE
        v_vote user_vote_post%rowtype;
    BEGIN
        SELECT *
        FROM user_vote_post
        WHERE userId = in_userId
          AND postId = in_postId
        LIMIT 1
        INTO v_vote;

        IF NOT FOUND THEN
            message := ''downvoted your post'';
            INSERT INTO user_vote_post (id, userId, postId, type)
            VALUES (nextval(''user_vote_post_sequence''), in_userId, in_postId, -1);
            INSERT INTO post_votes (postId, upvotes, downvotes)
            VALUES (in_postId, 0, 1)
            ON CONFLICT (postId)
                DO UPDATE
                SET downvotes = post_votes.downvotes + 1
            WHERE post_votes.postId = in_postId;
        ELSE
            IF v_vote.type = -1 THEN
                message := ''removed your downvote on the post'';
                DELETE
                FROM user_vote_post
                WHERE userId = in_userId
                  AND postId = in_postId;
                UPDATE post_votes
                SET downvotes = post_votes.downvotes - 1
                WHERE postId = in_postId;
            ELSE
                message := ''removed your upvote on the post, and downvoted it instead'';
                UPDATE user_vote_post
                SET type = -1
                WHERE userId = in_userId
                  AND postId = in_postId;
                UPDATE post_votes
                SET downvotes   = downvotes + 1,
                    upvotes = upvotes - 1
                WHERE postId = in_postId;
            END IF;
        END IF;
    END;
';

alter procedure downvote_post(varchar, varchar, out varchar) owner to maria;

create or replace procedure upvote_comment(IN in_userId character varying, IN in_commentId character varying, OUT message character varying)
    language plpgsql
as
'
    DECLARE
        v_vote user_vote_comment%rowtype;
    BEGIN
        SELECT *
        FROM user_vote_comment
        WHERE userId = in_userId
          AND commentId = in_commentId
        LIMIT 1
        INTO v_vote;

        IF NOT FOUND THEN
            message := ''upvoted your comment'';
            INSERT INTO user_vote_comment (id, userId, commentId, type)
            VALUES (nextval(''user_vote_comment_sequence''), in_userId, in_commentId, 1);
            INSERT INTO comment_votes (commentId, upvotes, downvotes)
            VALUES (in_commentId, 1, 0)
            ON CONFLICT (commentId)
                DO UPDATE
                SET upvotes = comment_votes.upvotes + 1
            WHERE comment_votes.commentId = in_commentId;
        ELSE
            IF v_vote.type = 1 THEN
                message := ''removed your upvote on the comment'';
                DELETE
                FROM user_vote_comment
                WHERE userId = in_userId
                  AND commentId = in_commentId;
                UPDATE comment_votes
                SET upvotes = upvotes - 1
                WHERE commentId = in_commentId;
            ELSE
                message := ''removed your downvote on the comment, and upvoted it instead'';
                UPDATE user_vote_comment
                SET type = 1
                WHERE userId = in_userId
                  AND commentId = in_commentId;
                UPDATE comment_votes
                SET upvotes   = upvotes + 1,
                    downvotes = downvotes - 1
                WHERE commentId = in_commentId;
            END IF;
        END IF;
    END;
';

alter procedure upvote_comment(varchar, varchar, out varchar) owner to maria;

create or replace procedure downvote_comment(IN in_userId character varying, IN in_commentId character varying, OUT message character varying)
    language plpgsql
as
'
    DECLARE
        v_vote user_vote_comment%rowtype;
    BEGIN
        SELECT *
        FROM user_vote_comment
        WHERE userId = in_userId
          AND commentId = in_commentId
        LIMIT 1
        INTO v_vote;

        IF NOT FOUND THEN
            message := ''downvoted your comment'';
            INSERT INTO user_vote_comment (id, userId, commentId, type)
            VALUES (nextval(''user_vote_comment_sequence''), in_userId, in_commentId, -1);
            INSERT INTO comment_votes (commentId, upvotes, downvotes)
            VALUES (in_commentId, 0, 1)
            ON CONFLICT (commentId)
                DO UPDATE
                SET downvotes = comment_votes.downvotes + 1
            WHERE comment_votes.commentId = in_commentId;
        ELSE
            IF v_vote.type = -1 THEN
                message := ''removed your downvote on the comment'';
                DELETE
                FROM user_vote_comment
                WHERE userId = in_userId
                  AND commentId = in_commentId;
                UPDATE comment_votes
                SET downvotes = comment_votes.downvotes - 1
                WHERE commentId = in_commentId;
            ELSE
                message := ''removed your upvote on the comment, and downvoted it instead'';
                UPDATE user_vote_comment
                SET type = -1
                WHERE userId = in_userId
                  AND commentId = in_commentId;
                UPDATE comment_votes
                SET downvotes   = downvotes + 1,
                    upvotes = upvotes - 1
                WHERE commentId = in_commentId;
            END IF;
        END IF;
    END;
';

alter procedure downvote_comment(varchar, varchar, out varchar) owner to maria;

create or replace procedure follow_reddit(IN in_redditId character varying, OUT message character varying)
    language plpgsql
as
'
    DECLARE
        follow reddit_followers%rowtype;
    BEGIN
        SELECT *
        FROM reddit_followers
        WHERE redditid = in_redditId
        LIMIT 1
        INTO follow;

        IF NOT FOUND THEN
            INSERT INTO reddit_followers (redditid, followercount)
            VALUES (in_redditId, 1);
            message := ''adding reddit'';

        ELSE
           UPDATE reddit_followers
            SET followercount   = followercount + 1
            WHERE redditid = in_redditId;
            message :=''reddit followed!'';
        END IF;
    END;
';

alter procedure follow_reddit(varchar, out varchar) owner to maria;
