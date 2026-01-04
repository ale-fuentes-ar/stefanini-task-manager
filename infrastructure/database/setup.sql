USE master;
GO
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'stefaninidb')
BEGIN
    CREATE DATABASE stefaninidb;
END
GO