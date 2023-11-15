CREATE VIEW covid_count AS
SELECT
    (SELECT COUNT(*) AS cases FROM covid_cases WHERE covid_case_status = 'confirmed') AS confirmed_total,
    (SELECT COUNT(*) AS cases FROM covid_cases WHERE covid_case_status = 'deceased') AS deceased_total,
    SUM(CASE WHEN covid_case_status = 'confirmed' THEN 1 ELSE 0 END) AS confirmed,
    SUM(CASE WHEN covid_case_status = 'suspect' THEN 1 ELSE 0 END) AS suspect,
    SUM(CASE WHEN covid_case_status = 'recovered' THEN 1 ELSE 0 END) AS recovered,
    SUM(CASE WHEN covid_case_status = 'deceased' THEN 1 ELSE 0 END) AS deceased
FROM covid_cases