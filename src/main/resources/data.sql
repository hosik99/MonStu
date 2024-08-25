-- PW_QUIZ 테이블에 데이터 삽입
INSERT INTO pw_quiz (quiz) VALUES ('Sample Quiz 1');
INSERT INTO pw_quiz (quiz) VALUES ('Sample Quiz 2');
INSERT INTO pw_quiz (quiz) VALUES ('Sample Quiz 3');
INSERT INTO pw_quiz (quiz) VALUES ('Sample Quiz 4');
INSERT INTO pw_quiz (quiz) VALUES ('Sample Quiz 5');

-- MEMBER 테이블에 데이터 삽입 Password is 'password'
INSERT INTO member (email, member_pw, role, qid) VALUES ('ADMIN@example.com', '{bcrypt}$2a$10$FcpH2G1uCVS87U3F0J/wSu/qKMHLJGe/bHlwPIv55F.8QqWyKMJS.', 'ROLE_ADMIN', 1);
INSERT INTO member (email, member_pw, role, qid) VALUES ('user2@example.com', '{bcrypt}$2a$10$FcpH2G1uCVS87U3F0J/wSu/qKMHLJGe/bHlwPIv55F.8QqWyKMJS.', 'ROLE_MEMBER', 2);
INSERT INTO member (email, member_pw, role, qid) VALUES ('user3@example.com', '{bcrypt}$2a$10$FcpH2G1uCVS87U3F0J/wSu/qKMHLJGe/bHlwPIv55F.8QqWyKMJS.', 'ROLE_MEMBER', 3);
INSERT INTO member (email, member_pw, role, qid) VALUES ('user4@example.com', '{bcrypt}$2a$10$FcpH2G1uCVS87U3F0J/wSu/qKMHLJGe/bHlwPIv55F.8QqWyKMJS.', 'ROLE_MEMBER', 4);
INSERT INTO member (email, member_pw, role, qid) VALUES ('user5@example.com', '{bcrypt}$2a$10$FcpH2G1uCVS87U3F0J/wSu/qKMHLJGe/bHlwPIv55F.8QqWyKMJS.', 'ROLE_MEMBER', 5);

-- MEMBER_INFO 테이블에 데이터 삽입
INSERT INTO member_info (member_id, created_at, birth, country, nickname) VALUES (1, '2024-08-10', '1990-01-01', 'Country1', 'Nickname1');
INSERT INTO member_info (member_id, created_at, birth, country, nickname) VALUES (2, '2024-08-10', '1991-02-02', 'Country2', 'Nickname2');
INSERT INTO member_info (member_id, created_at, birth, country, nickname) VALUES (3, '2024-08-10', '1992-03-03', 'Country3', 'Nickname3');
INSERT INTO member_info (member_id, created_at, birth, country, nickname) VALUES (4, '2024-08-10', '1993-04-04', 'Country4', 'Nickname4');
INSERT INTO member_info (member_id, created_at, birth, country, nickname) VALUES (5, '2024-08-10', '1994-05-05', 'Country5', 'Nickname5');

-- CONTENT 테이블에 데이터 삽입
INSERT INTO content (title, content, created_at, member_id) VALUES ('Palantir and Microsoft Partner to Deliver Enhanced Analytics and AI Services to Classified Networks for Critical National Security Operations', 'Palantir Technologies Inc. (NYSE: PLTR) and Microsoft Corporation (NASDAQ: MSFT) announce today a significant advancement in their partnership to bring some of the most sophisticated and secure cloud, AI and analytics capabilities to the U.S. Defense and Intelligence Community. This is a first-of-its-kind, integrated suite of technology that will allow critical national security missions to operationalize Microsoft’s best-in-class large language models (LLMs) via Azure OpenAI Service within Palantir’s AI Platforms (AIP) in Microsoft’s government and classified cloud environments.', '2024-08-10 10:00:00', 2);
INSERT INTO content (title, content, created_at, member_id) VALUES ('Title 2', 'Palantir and Microsoft have a long history operating in secure and accredited environments to deliver leading technology for the most critical U.S. Defense and Intelligence missions. Now, through this partnership, Palantir will deploy their suite of products – Foundry, Gotham, Apollo, and AIP – in Microsoft Azure Government and in the Azure Government Secret (DoD Impact Level 6) and Top Secret clouds. Palantir will also be an early adopter of Azure’s OpenAI Service in Microsoft’s Secret and Top Secret environments. This integrated solution of Microsoft’s Azure cloud compute and powerful language models (GPT-4 and others) with Palantir’s Foundry’s data integration and ontology capabilities and AIP’s use case building capabilities will enable operators to safely and responsibly build AI-driven operational workloads across Defense and Intelligence verticals — from logistics, to contracting, to prioritization and action planning, and more. Availability of the services is subject to completion of authorization and accreditation by appropriate government agencies.', '2024-08-10 11:00:00', 2);
INSERT INTO content (title, content, created_at, member_id) VALUES ('Title 3', '“Bringing Palantir and Microsoft capabilities to our national security apparatus is a step change in how we can support the defense and intelligence communities,” said Shyam Sankar, Chief Technology Officer, Palantir. “Palantir AIP has pioneered the approach to operationalizing AI value – beyond chat — across the enterprise. It’s our mission to deliver this software advantage and we’re thrilled to be the first industry partner to deploy Microsoft Azure OpenAI Service in classified environments.”', '2024-08-10 12:00:00', 3);
INSERT INTO content (title, content, created_at, member_id) VALUES ('Title 4', 'Content 4', '2024-08-10 13:00:00', 4);
INSERT INTO content (title, content, created_at, member_id) VALUES ('Title 5', 'Content 5', '2024-08-10 14:00:00', 5);

-- MY_WORD 테이블에 데이터 삽입
INSERT INTO my_word (target_word, translated_word, content_id) VALUES ('Word1', '단어1', 2);
INSERT INTO my_word (target_word, translated_word, content_id) VALUES ('Word2', '단어2', 2);
INSERT INTO my_word (target_word, translated_word, content_id) VALUES ('Word3', '단어3', 2);
INSERT INTO my_word (target_word, translated_word, content_id) VALUES ('Word4', '단어4', 4);
INSERT INTO my_word (target_word, translated_word, content_id) VALUES ('Word5', '단어5', 5);

-- MY_SENTENCE 테이블에 데이터 삽입
INSERT INTO my_sentence (sentence, type, content_id) VALUES ('Sentence1', 'Type1', 2);
INSERT INTO my_sentence (sentence, type, content_id) VALUES ('Sentence2', 'Type2', 2);
INSERT INTO my_sentence (sentence, type, content_id) VALUES ('Sentence3', 'Type3', 2);
INSERT INTO my_sentence (sentence, type, content_id) VALUES ('Sentence4', 'Type4', 4);
INSERT INTO my_sentence (sentence, type, content_id) VALUES ('Sentence5', 'Type5', 5);
