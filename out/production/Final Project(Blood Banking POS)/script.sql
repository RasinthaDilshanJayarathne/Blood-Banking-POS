DROP DATABASE IF EXISTS BloodBank;
CREATE DATABASE IF NOT EXISTS BloodBank;
SHOW DATABASES ;
USE BloodBank;

DROP TABLE IF EXISTS Users;
CREATE TABLE IF NOT EXISTS Users(
    uId VARCHAR (15),
    userName VARCHAR(45) NOT NULL DEFAULT 'Unknown',
    password VARCHAR(30) NOT NULL DEFAULT 'Unknown',
    possision VARCHAR (20),
    CONSTRAINT PRIMARY KEY (uId)
    );
SHOW TABLES ;
DESCRIBE Users;

DROP TABLE IF EXISTS Donor;
CREATE TABLE IF NOT EXISTS Donor(
    nic VARCHAR (15),
    uId VARCHAR (15),
    fullName VARCHAR(45) NOT NULL DEFAULT 'Unknown',
    address TEXT,
    city VARCHAR (15),
    bloodGroup VARCHAR (10),
    blId VARCHAR(15),
    gender VARCHAR (10),
    contact VARCHAR (15),
    email VARCHAR (30),
    CONSTRAINT PRIMARY KEY (nic),
    CONSTRAINT FOREIGN KEY (uId) REFERENCES Users(uId) ON DELETE CASCADE ON UPDATE CASCADE
    );
SHOW TABLES ;
DESCRIBE Donor;

DROP TABLE IF EXISTS Employee;
CREATE TABLE IF NOT EXISTS Employee(
    eId VARCHAR(15),
    userId VARCHAR (15),
    name VARCHAR(45) NOT NULL DEFAULT 'Unknown',
    address TEXT,
    city VARCHAR (15),
    employeeType VARCHAR (20),
    gender VARCHAR (10),
    contact VARCHAR (15),
    CONSTRAINT PRIMARY KEY (eId),
    CONSTRAINT FOREIGN KEY (userId) REFERENCES Users(uId) ON DELETE CASCADE ON UPDATE CASCADE
    );
SHOW TABLES ;
DESCRIBE Employee;

DROP TABLE IF EXISTS Blood;
CREATE TABLE IF NOT EXISTS Blood(
    blId VARCHAR(15),
    bloodGroup VARCHAR (5),
    description TEXT,
    CONSTRAINT PRIMARY KEY (blId)
    );
SHOW TABLES ;
DESCRIBE Blood;

DROP TABLE IF EXISTS `Donate Detail`;
CREATE TABLE IF NOT EXISTS `Donate Detail`(
    blId VARCHAR(15),
    rId VARCHAR(15),
    nic VARCHAR(15),
    date VARCHAR (10),
    time VARCHAR (10),
    QtyOnHand INT (5),
    totalQty INT (5),
    CONSTRAINT PRIMARY KEY (blId, nic,date,rId,time),
    CONSTRAINT FOREIGN KEY (blId) REFERENCES Blood(blId) ON DELETE CASCADE ON UPDATE CASCADE ,
    CONSTRAINT FOREIGN KEY (nic) REFERENCES Donor(nic) ON DELETE CASCADE ON UPDATE CASCADE
    );
SHOW TABLES ;
DESCRIBE `Donate Detail`;

DROP TABLE IF EXISTS Rack;
CREATE TABLE IF NOT EXISTS Rack(
    rId VARCHAR(15),
    blId VARCHAR(15),
    name VARCHAR (15),
    totalQty INT (5),
    bloodGroup VARCHAR (5),
    storeQty INT (5),
    CONSTRAINT PRIMARY KEY (rId),
    CONSTRAINT FOREIGN KEY (blId) REFERENCES Blood(blId) ON DELETE CASCADE ON UPDATE CASCADE
    );
SHOW TABLES ;
DESCRIBE Rack;

DROP TABLE IF EXISTS Hospital;
CREATE TABLE IF NOT EXISTS Hospital(
    hId VARCHAR(15),
    name VARCHAR (15),
    email VARCHAR (30),
    address TEXT,
    city VARCHAR (15),
    contact VARCHAR (20),
    webSite VARCHAR (30),
    CONSTRAINT PRIMARY KEY (hId)
    );
SHOW TABLES ;
DESCRIBE Hospital;

DROP TABLE IF EXISTS Orders;
CREATE TABLE IF NOT EXISTS Orders(
    oId VARCHAR(15),
    hId VARCHAR(15),
    orderDate VARCHAR (20),
    time VARCHAR(25),
    CONSTRAINT PRIMARY KEY (oId),
    CONSTRAINT FOREIGN KEY (hId) REFERENCES Hospital(hId) ON DELETE CASCADE ON UPDATE CASCADE
    );
SHOW TABLES ;
DESCRIBE Orders;

DROP TABLE IF EXISTS `Order Detail`;
CREATE TABLE IF NOT EXISTS `Order Detail`(
    rId VARCHAR(15),
    oId VARCHAR(15),
    qty INT,
    date VARCHAR (20),
    time VARCHAR (20),
    CONSTRAINT PRIMARY KEY (oId, rId),
    CONSTRAINT FOREIGN KEY (oId) REFERENCES Orders(oId) ON DELETE CASCADE ON UPDATE CASCADE ,
    CONSTRAINT FOREIGN KEY (rId) REFERENCES Rack(rId) ON DELETE CASCADE ON UPDATE CASCADE
    );
SHOW TABLES ;
DESCRIBE `Order Detail`;

DROP TABLE IF EXISTS Report;
CREATE TABLE IF NOT EXISTS Report(
    reId VARCHAR(15),
    uId VARCHAR(15),
    type VARCHAR (30),
    date VARCHAR (20),
    time VARCHAR (20),
    CONSTRAINT PRIMARY KEY (reId),
    CONSTRAINT FOREIGN KEY (uId) REFERENCES Users(uId) ON DELETE CASCADE ON UPDATE CASCADE
    );
SHOW TABLES ;
DESCRIBE Report;

---------------------- Full Outer -------------------------
SELECT * FROM `donate detail` LEFT JOIN `store detail` ON `donate detail`.blId = `store detail`.blId
UNION ALL
SELECT * FROM `donate detail` RIGHT JOIN `store detail` ON `donate detail`.blId = `store detail`.blId

SELECT * FROM rack LEFT JOIN `store detail` ON rack.rId = `store detail`.rId UNION ALL SELECT * FROM rack RIGHT JOIN `store detail` ON rack.rId = `store detail`.rId


SELECT *
FROM `donate detail` FULL OUTER JOIN `store detail` ON `donate detail`.blId = `store detail`.blId WHERE blId="B00-0004";

--------------------- Inner Join --------------------------
SELECT * FROM rack INNER JOIN `store detail` ON rack.rId = `store detail`.rId;


SELECT r.bloodGroup, r.rId, r.name, r.totalQty, dn.QtyOnHand
FROM  Rack r INNER JOIN Blood b ON r.blId = b.blId INNER JOIN Donor d ON d.blId = b.blId INNER JOIN `Donate Detail` dn ON r.name = r.name;

SELECT r.bloodGroup, r.rId, r.name, r.totalQty, dn.QtyOnHand
FROM  Rack r INNER JOIN Blood b ON r.blId = b.blId INNER JOIN Donor d ON d.blId = b.blId INNER JOIN `Donate Detail` dn ON dn.blId = b.blId;

INSERT INTO  Blood VALUES ('B00-0001','A+','AAAAAAA');
INSERT INTO  Blood VALUES ('B00-0002','A-','AAAAAAA');
INSERT INTO  Blood VALUES ('B00-0003','O+','AAAAAAA');

INSERT INTO  Rack VALUES ('R00-0001','B00-0003','O-positive-i','O+','20');
INSERT INTO  Rack VALUES ('R00-0002','B00-0002','A-negetive-i','A-','10');
INSERT INTO  Rack VALUES ('R00-0003','B00-0001','A-positive-i','A+','30');
INSERT INTO  Rack VALUES ('R00-0004','B00-0001','A-positive-ii','A+','30');

INSERT INTO  Donor VALUES ('123123123123','U00-0001','Nima','No/12','Galle','A+','B00-0001','male','091-2345432','niml@gmail.com');
INSERT INTO  Donor VALUES ('123123222223','U00-0001','Kamal','No.125','Matara','A-','B00-0002','male','091-2366632','kamal@gmail.com');
INSERT INTO  Donor VALUES ('123125653423','U00-0001','Wimal','No/1111','Panadura','O+','B00-0003','male','091-2344442','wimal@gmail.com');

INSERT INTO  Users VALUES ('U00-0001','Bandara','12345','Reception');

INSERT INTO `Donate Detail` VALUES ('B00-0001','R00-0001','123125653423','23/09/2021','11:34 AM','5');
INSERT INTO `Donate Detail` VALUES ('B00-0002','R00-0002','123123222223','23/09/2021','11:34 AM','2');

SELECT r.bloodGroup, r.rId, r.name, r.totalQty,
FROM  Rack r INNER JOIN Blood b ON r.blId = b.blId INNER JOIN Donor d ON d.blId = b.blId

SELECT r.bloodGroup, r.rId, r.name, r.totalQty, dn.QtyOnHand
FROM  Rack r INNER JOIN `Donate Detail` dn ON r.blId = dn.blId;

SELECT r.bloodGroup, r.rId, r.name, r.totalQty, dn.QtyOnHand
FROM  Rack r LEFT JOIN `Donate Detail` dn ON r.doId = dn.doId;


----------------------------------------------------------------------
SELECT r.blId, r.bloodGroup, r.rId, r.name, r.totalQty, dn.QtyOnHand
FROM  Rack r LEFT JOIN `Donate Detail` dn ON r.rId = dn.rId;

SELECT r.name,dn.QtyOnHand
FROM  Rack r LEFT JOIN `Donate Detail` dn ON r.rId = dn.rId;

----------------------------------------------------------------------
select totalQty from `donate detail` where rId="R00-0004" ORDER BY totalQty DESC LIMIT 1;

SELECT r.blId, r.bloodGroup, r.rId, r.name, r.totalQty, SUM(dn.QtyOnHand)
FROM  Rack r LEFT JOIN `Donate Detail` dn ON r.rId = dn.rId GROUP BY r.rId;

SELECT r.name,SUM (dn.QtyOnHand) FROM  Rack r LEFT JOIN `Donate Detail` dn ON r.rId = dn.rId where name="O-positive-i";

SELECT r.rId,r.blId,dn.QtyOnHand,dn.date,dn.time
FROM  Rack r LEFT JOIN `Donate Detail` dn ON r.rId = dn.rId;

SELECT * FROM `Donate Detail` WHERE fullName LIKE '%"+value+"%' or city LIKE '%"+value+"%'

SELECT r.bloodGroup, r.rId, r.name, r.totalQty,dn.totalQty
FROM  Rack r LEFT JOIN `Donate Detail` dn ON r.rId = dn.rId GROUP BY r.rId LIKE '%"+value+"%' or name LIKE '%"+value+"%';

SELECT r.bloodGroup, r.rId, r.name, r.totalQty,dn.totalQty
FROM  Rack r LEFT JOIN `Donate Detail` dn ON r.rId = dn.rId r.rId LIKE '%"+value+"%' or name LIKE '%"+value+"%';

-------------------------------------------------------------------------

UPDATE `donate detail` SET `donate detail`.QtyOnHand=QtyOnHand-2 FROM `donate detail` LEFT JOIN Rack ON Rack.rId = `donate detail`.rId WHERE name="O-positive-i";

UPDATE `donate detail` SET `donate detail`.QtyOnHand=QtyOnHand-2 FROM `donate detail` LEFT JOIN Rack ON Rack.rId=`donate detail`.rId WHERE name="O-positive-i";

UPDATE Rack r LEFT JOIN `donate detail` dn ON dn.rId = dn.rid SET dn.QtyOnHand =? WHERE dn.blId =? AND dn.rId =?;

SELECT *
FROM `donate detail`
WHERE time=' 21:05 PM';
GROUP BY date
ORDER BY COUNT(QtyOnHand) DESC;