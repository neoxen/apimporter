----------------------------------------------
-- licensing analysis
----------------------------------------------

SELECT * FROM ap_administrative_licensing_temp WHERE object_name IS NULL;

SELECT * FROM ap_administrative_licensing_temp WHERE object_name LIKE '%测试%';
SELECT * FROM ap_administrative_licensing_temp WHERE LENGTH(TRIM(object_name)) = 0;
SELECT * FROM ap_administrative_licensing_temp WHERE object_name LIKE '%***%';
SELECT * FROM ap_administrative_licensing_temp WHERE LENGTH(TRIM(object_name)) < 6 ORDER BY LENGTH(TRIM(object_name)) ;
SELECT * FROM ap_administrative_licensing_temp
WHERE credit_code IS NULL
AND org_code IS NULL
AND ic_code IS NULL
AND tax_code IS NULL
AND identity_code IS NULL
ORDER BY LENGTH(TRIM(object_name)) ;
SELECT * FROM ap_administrative_licensing_temp
WHERE LENGTH(TRIM(object_name)) > 12
AND LENGTH(TRIM(identity_code)) < 20
AND legal_rep IS NOT NULL
AND credit_code IS NULL
AND org_code IS NULL
AND ic_code IS NULL
AND tax_code IS NULL
ORDER BY LENGTH(TRIM(object_name)) ;
SELECT * FROM ap_administrative_licensing_temp
WHERE LENGTH(TRIM(object_name)) < 12
AND identity_code IS NULL
ORDER BY LENGTH(TRIM(object_name)) ;
SELECT * FROM ap_administrative_licensing_temp
WHERE LENGTH(TRIM(credit_code)) < 18
AND credit_code IS NOT NULL
AND credit_code NOT LIKE '-'
AND credit_code NOT LIKE '——'
AND LENGTH(TRIM(credit_code)) <> 0
ORDER BY LENGTH(TRIM(object_name)) ;
SELECT *  FROM ap_administrative_licensing_temp
WHERE LENGTH(TRIM(credit_code)) > 18
AND credit_code IS NOT NULL
AND credit_code NOT LIKE '-'
AND credit_code NOT LIKE '——'
AND LENGTH(TRIM(credit_code)) <> 0
ORDER BY LENGTH(TRIM(object_name)) ;
SELECT *  FROM ap_administrative_licensing_temp
WHERE LENGTH(TRIM(ic_code)) <> 15
AND LENGTH(TRIM(ic_code)) <> 13
AND ic_code IS NOT NULL
AND ic_code NOT LIKE '-'
AND ic_code NOT LIKE '——'
AND ic_code NOT LIKE '　'
AND LENGTH(TRIM(ic_code)) <> 0
ORDER BY LENGTH(TRIM(ic_code)) ;
SELECT * FROM ap_administrative_licensing_temp
WHERE LENGTH(TRIM(tax_code)) <> 18
AND LENGTH(TRIM(tax_code)) <> 15
AND tax_code IS NOT NULL
AND tax_code NOT LIKE '-'
AND tax_code NOT LIKE '——'
AND tax_code NOT LIKE '　'
AND LENGTH(TRIM(tax_code)) <> 0
ORDER BY LENGTH(TRIM(tax_code)) ;
SELECT * FROM ap_administrative_licensing_temp
WHERE  LENGTH(TRIM(org_code)) < 8
AND org_code IS NOT NULL
AND org_code NOT LIKE '-'
AND org_code NOT LIKE '——'
AND org_code NOT LIKE '　'
AND org_code NOT LIKE '--%'
AND org_code NOT LIKE '*%'
AND org_code NOT LIKE '未办'
AND org_code NOT LIKE '零'
AND org_code NOT LIKE '暂无'
AND org_code NOT LIKE '0'
AND org_code NOT LIKE '1'
AND LENGTH(TRIM(org_code)) > 1
ORDER BY LENGTH(TRIM(org_code)) ;
SELECT * FROM ap_administrative_licensing_temp
WHERE  LENGTH(TRIM(org_code)) > 13
AND org_code IS NOT NULL
AND org_code NOT LIKE '-'
AND org_code NOT LIKE '——'
AND org_code NOT LIKE '　'
AND org_code NOT LIKE '--%'
AND org_code NOT LIKE '*%'
AND org_code NOT LIKE '未办'
AND org_code NOT LIKE '零'
AND org_code NOT LIKE '暂无'
AND org_code NOT LIKE '%(%'
AND org_code NOT LIKE '%/%'
AND org_code NOT LIKE '%（%'
AND org_code NOT LIKE '0'
AND org_code NOT LIKE '1'
AND LENGTH(TRIM(org_code)) > 1
ORDER BY LENGTH(TRIM(org_code)) ;

SELECT * FROM ap_administrative_licensing_temp
WHERE  LENGTH(TRIM(identity_code)) < 15
AND LENGTH(TRIM(identity_code)) > 1
ORDER BY LENGTH(TRIM(object_name)) , LENGTH(TRIM(identity_code)) ;

----------------------------------------------
-- licensing reduction
----------------------------------------------
SELECT COUNT(*) FROM `ap_administrative_licensing_temp_1`;
DELETE FROM ap_administrative_licensing_temp_1 WHERE object_name IS NULL;
SELECT * FROM ap_administrative_licensing_temp_1 WHERE object_name IS NULL;
SELECT * FROM ap_administrative_licensing_temp_1 WHERE object_name LIKE '%测试%';
DELETE FROM ap_administrative_licensing_temp_1 WHERE LENGTH(TRIM(object_name)) = 0;
DELETE FROM ap_administrative_licensing_temp_1 WHERE object_name LIKE '%***%';
SELECT * FROM ap_administrative_licensing_temp_1 WHERE LENGTH(TRIM(object_name)) < 6 ORDER BY LENGTH(TRIM(object_name)) ;
DELETE FROM ap_administrative_licensing_temp_1
WHERE credit_code IS NULL
AND org_code IS NULL
AND ic_code IS NULL
AND tax_code IS NULL
AND identity_code IS NULL
ORDER BY LENGTH(TRIM(object_name)) ;
DELETE FROM ap_administrative_licensing_temp_1
WHERE LENGTH(TRIM(object_name)) < 12
AND identity_code IS NULL
ORDER BY LENGTH(TRIM(object_name)) ;
DELETE FROM ap_administrative_licensing_temp_1
WHERE LENGTH(TRIM(credit_code)) < 18
AND credit_code IS NOT NULL
AND credit_code NOT LIKE '-'
AND credit_code NOT LIKE '——'
AND LENGTH(TRIM(credit_code)) <> 0
ORDER BY LENGTH(TRIM(object_name)) ;
DELETE  FROM ap_administrative_licensing_temp_1
WHERE LENGTH(TRIM(credit_code)) > 18
AND credit_code IS NOT NULL
AND credit_code NOT LIKE '-'
AND credit_code NOT LIKE '——'
AND LENGTH(TRIM(credit_code)) <> 0
ORDER BY LENGTH(TRIM(object_name)) ;
DELETE  FROM ap_administrative_licensing_temp_1
WHERE LENGTH(TRIM(ic_code)) <> 15
AND LENGTH(TRIM(ic_code)) <> 13
AND ic_code IS NOT NULL
AND ic_code NOT LIKE '-'
AND ic_code NOT LIKE '——'
AND ic_code NOT LIKE '　'
AND LENGTH(TRIM(ic_code)) <> 0
ORDER BY LENGTH(TRIM(ic_code)) ;
DELETE FROM ap_administrative_licensing_temp_1
WHERE LENGTH(TRIM(tax_code)) <> 18
AND LENGTH(TRIM(tax_code)) <> 15
AND tax_code IS NOT NULL
AND tax_code NOT LIKE '-'
AND tax_code NOT LIKE '——'
AND tax_code NOT LIKE '　'
AND LENGTH(TRIM(tax_code)) <> 0
ORDER BY LENGTH(TRIM(tax_code)) ;
DELETE FROM ap_administrative_licensing_temp_1
WHERE  LENGTH(TRIM(org_code)) < 8
AND org_code IS NOT NULL
AND org_code NOT LIKE '-'
AND org_code NOT LIKE '——'
AND org_code NOT LIKE '　'
AND org_code NOT LIKE '--%'
AND org_code NOT LIKE '*%'
AND org_code NOT LIKE '未办'
AND org_code NOT LIKE '零'
AND org_code NOT LIKE '暂无'
AND org_code NOT LIKE '0'
AND org_code NOT LIKE '1'
AND LENGTH(TRIM(org_code)) > 1
ORDER BY LENGTH(TRIM(org_code)) ;
DELETE FROM ap_administrative_licensing_temp_1
WHERE  LENGTH(TRIM(org_code)) > 13
AND org_code IS NOT NULL
AND org_code NOT LIKE '-'
AND org_code NOT LIKE '——'
AND org_code NOT LIKE '　'
AND org_code NOT LIKE '--%'
AND org_code NOT LIKE '*%'
AND org_code NOT LIKE '未办'
AND org_code NOT LIKE '零'
AND org_code NOT LIKE '暂无'
AND org_code NOT LIKE '%(%'
AND org_code NOT LIKE '%/%'
AND org_code NOT LIKE '%（%'
AND org_code NOT LIKE '0'
AND org_code NOT LIKE '1'
AND LENGTH(TRIM(org_code)) > 1
ORDER BY LENGTH(TRIM(org_code)) ;

DELETE FROM ap_administrative_licensing_temp_1
WHERE LENGTH(TRIM(object_name)) > 12
AND LENGTH(TRIM(identity_code)) < 20
AND legal_rep IS NOT NULL
AND credit_code IS NULL
AND org_code IS NULL
AND ic_code IS NULL
AND tax_code IS NULL
ORDER BY LENGTH(TRIM(object_name)) ;


SELECT * FROM ap_administrative_licensing_temp_1
WHERE  LENGTH(TRIM(identity_code)) > 13
AND identity_code IS NOT NULL
AND identity_code NOT LIKE '-'
AND identity_code NOT LIKE '——'
AND identity_code NOT LIKE '　'
AND identity_code NOT LIKE '--%'
AND identity_code NOT LIKE '*%'
AND identity_code NOT LIKE '未办'
AND identity_code NOT LIKE '零'
AND identity_code NOT LIKE '暂无'
AND identity_code NOT LIKE '%(%'
AND identity_code NOT LIKE '%/%'
AND identity_code NOT LIKE '%（%'
AND identity_code NOT LIKE '0'
AND identity_code NOT LIKE '1'
AND LENGTH(TRIM(identity_code)) > 1
ORDER BY LENGTH(TRIM(identity_code)) ;


SELECT * FROM ap_administrative_licensing_temp WHERE LENGTH(TRIM(object_name)) = 0;

SELECT * FROM ap_administrative_licensing_temp WHERE LENGTH(TRIM(licensing_organ)) = 0;

SELECT * FROM ap_administrative_licensing_temp WHERE licensing_organ IS NULL;

SELECT * FROM ap_administrative_licensing_temp WHERE object_name IS NOT NULL;

SELECT * FROM ap_administrative_licensing_temp WHERE object_name LIKE '%est%';

SELECT * FROM ap_administrative_licensing_temp WHERE object_name LIKE '%*%';

SELECT * FROM ap_organ WHERE id =  53;

SELECT * FROM ap_administrative_licensing_temp WHERE LENGTH(TRIM(object_name)) < 6 ORDER BY LENGTH(TRIM(object_name)) ;

SELECT COUNT(*) FROM `ap_administrative_penalty_temp`;

----------------------------------------------
-- penalty analysis
----------------------------------------------
SELECT * FROM ap_administrative_penalty_temp;
SELECT * FROM ap_administrative_penalty_temp WHERE object_name IS NULL;

SELECT * FROM ap_administrative_penalty_temp WHERE object_name LIKE '%测试%';
SELECT * FROM ap_administrative_penalty_temp WHERE LENGTH(TRIM(object_name)) = 0;
SELECT * FROM ap_administrative_penalty_temp WHERE object_name LIKE '%*%';
SELECT * FROM ap_administrative_penalty_temp WHERE LENGTH(TRIM(object_name)) < 6 ORDER BY LENGTH(TRIM(object_name)) ;
SELECT * FROM ap_administrative_penalty_temp
WHERE credit_code IS NULL
AND org_code IS NULL
AND ic_code IS NULL
AND tax_code IS NULL
AND identity_code IS NULL
ORDER BY LENGTH(TRIM(object_name)) ;
SELECT * FROM ap_administrative_penalty_temp
WHERE LENGTH(TRIM(object_name)) < 12
AND identity_code IS NULL
ORDER BY LENGTH(TRIM(object_name)) ;
SELECT * FROM ap_administrative_penalty_temp
WHERE LENGTH(TRIM(object_name)) < 12
AND LENGTH(TRIM(identity_code)) = 0
ORDER BY LENGTH(TRIM(object_name)) ;
SELECT * FROM ap_administrative_penalty_temp
WHERE LENGTH(TRIM(credit_code)) < 15
AND credit_code IS NOT NULL
AND credit_code NOT LIKE '-'
AND credit_code NOT LIKE '——'
AND credit_code NOT LIKE '无'
AND LENGTH(TRIM(credit_code)) <> 0
ORDER BY LENGTH(TRIM(object_name)) , LENGTH(TRIM(credit_code));
SELECT *  FROM ap_administrative_penalty_temp
WHERE LENGTH(TRIM(credit_code)) > 18
AND credit_code IS NOT NULL
AND credit_code NOT LIKE '-'
AND credit_code NOT LIKE '——'
AND LENGTH(TRIM(credit_code)) <> 0
ORDER BY LENGTH(TRIM(object_name)) ;
SELECT *  FROM ap_administrative_penalty_temp
WHERE LENGTH(TRIM(ic_code)) <> 15
AND LENGTH(TRIM(ic_code)) < 12
AND ic_code IS NOT NULL
AND ic_code NOT LIKE '-'
AND ic_code NOT LIKE '——'
AND ic_code NOT LIKE '　'
AND ic_code NOT LIKE '无'
AND LENGTH(TRIM(ic_code)) <> 0
ORDER BY LENGTH(TRIM(ic_code)) ;
SELECT * FROM ap_administrative_penalty_temp
WHERE LENGTH(TRIM(tax_code)) <> 18
AND LENGTH(TRIM(tax_code)) <> 15
AND tax_code IS NOT NULL
AND tax_code NOT LIKE '-'
AND tax_code NOT LIKE '——'
AND tax_code NOT LIKE '　'
AND tax_code NOT LIKE '无'
AND LENGTH(TRIM(tax_code)) <> 0
ORDER BY LENGTH(TRIM(tax_code)) ;
SELECT * FROM ap_administrative_penalty_temp
WHERE  LENGTH(TRIM(org_code)) < 8
AND org_code IS NOT NULL
AND org_code NOT LIKE '-'
AND org_code NOT LIKE '——'
AND org_code NOT LIKE '　'
AND org_code NOT LIKE '--%'
AND org_code NOT LIKE '*%'
AND org_code NOT LIKE '未办'
AND org_code NOT LIKE '零'
AND org_code NOT LIKE '暂无'
AND org_code NOT LIKE '无'
AND org_code NOT LIKE '0'
AND org_code NOT LIKE '1'
AND LENGTH(TRIM(org_code)) > 1
ORDER BY LENGTH(TRIM(org_code)) ;
SELECT * FROM ap_administrative_penalty_temp
WHERE  LENGTH(TRIM(org_code)) > 13
AND org_code IS NOT NULL
AND org_code NOT LIKE '-'
AND org_code NOT LIKE '——'
AND org_code NOT LIKE '　'
AND org_code NOT LIKE '--%'
AND org_code NOT LIKE '*%'
AND org_code NOT LIKE '未办'
AND org_code NOT LIKE '零'
AND org_code NOT LIKE '暂无'
AND org_code NOT LIKE '%(%'
AND org_code NOT LIKE '%/%'
AND org_code NOT LIKE '%（%'
AND org_code NOT LIKE '0'
AND org_code NOT LIKE '1'
AND LENGTH(TRIM(org_code)) > 1
ORDER BY LENGTH(TRIM(org_code)) ;

SELECT * FROM ap_administrative_penalty_temp
WHERE  LENGTH(TRIM(identity_code)) < 15
AND LENGTH(TRIM(identity_code)) > 1
ORDER BY LENGTH(TRIM(object_name)) , LENGTH(TRIM(identity_code)) ;

SELECT * FROM ap_administrative_penalty_temp
WHERE LENGTH(TRIM(object_name)) > 12
AND LENGTH(TRIM(identity_code)) < 20
AND legal_rep IS NOT NULL
AND credit_code IS NULL
AND org_code IS NULL
AND ic_code IS NULL
AND tax_code IS NULL
ORDER BY LENGTH(TRIM(object_name)) ;


----------------------------------------------
-- penalty reduction
----------------------------------------------
SELECT * FROM ap_administrative_penalty_temp_1;
DELETE FROM ap_administrative_penalty_temp_1 WHERE object_name IS NULL;

DELETE FROM ap_administrative_penalty_temp_1 WHERE LENGTH(TRIM(object_name)) = 0;

DELETE FROM ap_administrative_penalty_temp_1
WHERE credit_code IS NULL
AND org_code IS NULL
AND ic_code IS NULL
AND tax_code IS NULL
AND identity_code IS NULL
ORDER BY LENGTH(TRIM(object_name)) ;

DELETE FROM ap_administrative_penalty_temp_1
WHERE LENGTH(TRIM(object_name)) < 12
AND identity_code IS NULL
ORDER BY LENGTH(TRIM(object_name)) ;

DELETE * FROM ap_administrative_penalty_temp_1
WHERE LENGTH(TRIM(object_name)) < 12
AND LENGTH(TRIM(identity_code)) = 0
ORDER BY LENGTH(TRIM(object_name)) ;

DELETE FROM ap_administrative_penalty_temp_1
WHERE LENGTH(TRIM(credit_code)) < 15
AND credit_code IS NOT NULL
AND credit_code NOT LIKE '-'
AND credit_code NOT LIKE '——'
AND credit_code NOT LIKE '无'
AND LENGTH(TRIM(credit_code)) <> 0
ORDER BY LENGTH(TRIM(object_name)) , LENGTH(TRIM(credit_code));


DELETE FROM ap_administrative_penalty_temp_1
WHERE LENGTH(TRIM(ic_code)) <> 15
AND LENGTH(TRIM(ic_code)) < 12
AND ic_code IS NOT NULL
AND ic_code NOT LIKE '-'
AND ic_code NOT LIKE '——'
AND ic_code NOT LIKE '　'
AND ic_code NOT LIKE '无'
AND LENGTH(TRIM(ic_code)) <> 0
ORDER BY LENGTH(TRIM(ic_code)) ;

DELETE FROM ap_administrative_penalty_temp_1
WHERE LENGTH(TRIM(object_name)) > 12
AND LENGTH(TRIM(identity_code)) < 20
AND legal_rep IS NOT NULL
AND credit_code IS NULL
AND org_code IS NULL
AND ic_code IS NULL
AND tax_code IS NULL
ORDER BY LENGTH(TRIM(object_name)) ;

SELECT * FROM ap_administrative_penalty_temp_1
WHERE LENGTH(TRIM(tax_code)) <> 18
AND LENGTH(TRIM(tax_code)) <> 15
AND tax_code IS NOT NULL
AND tax_code NOT LIKE '-'
AND tax_code NOT LIKE '——'
AND tax_code NOT LIKE '　'
AND tax_code NOT LIKE '无'
AND LENGTH(TRIM(tax_code)) <> 0
ORDER BY LENGTH(TRIM(tax_code)) ;

SELECT * FROM ap_administrative_penalty_temp_1
WHERE  LENGTH(TRIM(org_code)) < 8
AND org_code IS NOT NULL
AND org_code NOT LIKE '-'
AND org_code NOT LIKE '——'
AND org_code NOT LIKE '　'
AND org_code NOT LIKE '--%'
AND org_code NOT LIKE '*%'
AND org_code NOT LIKE '未办'
AND org_code NOT LIKE '零'
AND org_code NOT LIKE '暂无'
AND org_code NOT LIKE '无'
AND org_code NOT LIKE '0'
AND org_code NOT LIKE '1'
AND LENGTH(TRIM(org_code)) > 1
ORDER BY LENGTH(TRIM(org_code)) ;
SELECT * FROM ap_administrative_penalty_temp_1
WHERE  LENGTH(TRIM(org_code)) > 13
AND org_code IS NOT NULL
AND org_code NOT LIKE '-'
AND org_code NOT LIKE '——'
AND org_code NOT LIKE '　'
AND org_code NOT LIKE '--%'
AND org_code NOT LIKE '*%'
AND org_code NOT LIKE '未办'
AND org_code NOT LIKE '零'
AND org_code NOT LIKE '暂无'
AND org_code NOT LIKE '%(%'
AND org_code NOT LIKE '%/%'
AND org_code NOT LIKE '%（%'
AND org_code NOT LIKE '0'
AND org_code NOT LIKE '1'
AND LENGTH(TRIM(org_code)) > 1
ORDER BY LENGTH(TRIM(org_code)) ;

SELECT * FROM ap_administrative_penalty_temp_1
WHERE  LENGTH(TRIM(identity_code)) < 15
AND LENGTH(TRIM(identity_code)) > 1
ORDER BY LENGTH(TRIM(object_name)) , LENGTH(TRIM(identity_code)) ;