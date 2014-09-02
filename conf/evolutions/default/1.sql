# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "charge" ("id" VARCHAR(254) NOT NULL PRIMARY KEY,"description" VARCHAR(254) NOT NULL,"status" VARCHAR(254) NOT NULL,"amount" DOUBLE PRECISION NOT NULL,"userID" VARCHAR(254) NOT NULL);
create table "logininfo" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"providerID" VARCHAR(254) NOT NULL,"providerKey" VARCHAR(254) NOT NULL);
create table "oauth1info" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"token" VARCHAR(254) NOT NULL,"secret" VARCHAR(254) NOT NULL,"loginInfoId" BIGINT NOT NULL);
create table "oauth2info" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"accesstoken" VARCHAR(254) NOT NULL,"tokentype" VARCHAR(254),"expiresin" INTEGER,"refreshtoken" VARCHAR(254),"logininfoid" BIGINT NOT NULL);
create table "passwordinfo" ("hasher" VARCHAR(254) NOT NULL,"password" VARCHAR(254) NOT NULL,"salt" VARCHAR(254),"loginInfoId" BIGINT NOT NULL);
create table "product" ("id" BIGSERIAL PRIMARY KEY,"name" VARCHAR(254) NOT NULL,"description" VARCHAR(254) NOT NULL,"price" DOUBLE PRECISION NOT NULL);
create table "subscription" ("id" SERIAL NOT NULL PRIMARY KEY,"conektaSubscriptionID" VARCHAR(254) NOT NULL,"userID" VARCHAR(254) NOT NULL,"planID" VARCHAR(254) NOT NULL);
create table "user" ("userID" VARCHAR(254) NOT NULL PRIMARY KEY,"firstName" VARCHAR(254),"lastName" VARCHAR(254),"fullName" VARCHAR(254),"email" VARCHAR(254),"avatarURL" VARCHAR(254),"conektaUserId" VARCHAR(254));
create table "userlogininfo" ("userID" VARCHAR(254) NOT NULL,"loginInfoId" BIGINT NOT NULL);

# --- !Downs

drop table "userlogininfo";
drop table "user";
drop table "subscription";
drop table "product";
drop table "passwordinfo";
drop table "oauth2info";
drop table "oauth1info";
drop table "logininfo";
drop table "charge";

