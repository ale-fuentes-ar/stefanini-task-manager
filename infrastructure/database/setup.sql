IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'stefaninidb')
BEGIN
  CREATE DATABASE stefaninidb;
END
GO