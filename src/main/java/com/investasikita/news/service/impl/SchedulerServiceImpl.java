package com.investasikita.news.service.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.investasikita.news.domain.enumeration.ArticleStatus;
import com.investasikita.news.domain.enumeration.QueueStatus;
import com.investasikita.news.service.*;
import com.investasikita.news.service.dto.ArticleDTO;
import com.investasikita.news.service.dto.ArticleQueueDTO;
import com.investasikita.news.service.dto.HeadlineDTO;
import com.vladmihalcea.concurrent.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Service Implementation for managing Product.
 */
@Service
@Transactional
public class SchedulerServiceImpl implements SchedulerService {

    private final Logger log = LoggerFactory.getLogger(SchedulerServiceImpl.class);

    private final ArticleService articleService;
    private final ArticleQueueService articleQueueService;
    private final HeadlineService headlineService;
    private final NewsMgmtService newsMgmtService;

    private static final String SCHEDULER_ARTICLE_UPDATE = "scheduler.article_update";
    private static final String SCHEDULER_ARTICLE_QUEUE = "scheduler.article_queue";
    private static final String SCHEDULER_HEADLINE_STARTDATE = "scheduler.headline_startdate";
    private static final String SCHEDULER_HEADLINE_ENDDATE = "scheduler.headline_enddate";

    @Autowired
    HazelcastInstance hazelcast;

    public SchedulerServiceImpl(
        ArticleService articleService,
        ArticleQueueService articleQueueService,
        HeadlineService headlineService,
        NewsMgmtService newsMgmtService
    ) {
        this.articleService = articleService;
        this.articleQueueService = articleQueueService;
        this.headlineService = headlineService;
        this.newsMgmtService = newsMgmtService;
    }

    @Transactional(readOnly = true)
    private Page<ArticleQueueDTO> checkArticleQueue(Pageable page) {
        log.debug("Request to check Article Queue schedule");

        Instant iStartDate = Instant.now().minus(1, ChronoUnit.DAYS);
        Instant currentDate = Instant.now();

        return articleQueueService.checkPublishDate(currentDate, page);
    }

    @Transactional(readOnly = true)
    private Page<HeadlineDTO> checkHeadlineSettingStartDateSchedule(Pageable page) {
        log.debug("Request to check Headline setting Start Date schedule");

//        Instant iStartDate = Instant.now().minus(1, ChronoUnit.DAYS);
//        Instant iToDate = Instant.now();
        Instant currentDate = Instant.now();

        return headlineService.checkHeadlineScheduleStartDate(currentDate, page);
    }

    @Transactional(readOnly = true)
    private Page<HeadlineDTO> checkHeadlineSettingEndDateSchedule(Pageable page) {
        log.debug("Request to check Headline setting End Date schedule");

//        Instant iStartDate = Instant.now().minus(1, ChronoUnit.DAYS);
//        Instant iToDate = Instant.now();
        Instant currentDate = Instant.now();

        return headlineService.checkHeadlineScheduleEndDate(currentDate, page);
    }

    @Override
    @Transactional
//    @Scheduled(fixedRate = 1000 * 60 * 62)
//    @Scheduled(cron = "* */30 * * * *")
    @Scheduled(fixedDelay = 1000 * 60 * 60)
    @Retry(times = 5, on = org.hibernate.TransactionException.class)
    public Boolean updateArticleScheduleIntegration() throws IOException {
        if (randomDelay(SchedulerServiceImpl.SCHEDULER_ARTICLE_UPDATE) < 0)
            return false;

        return newsMgmtService.reload(false);
    }

        @Override
    @Transactional
//    @Scheduled(cron = "0/10 0 * * * *")
//    @Scheduled(cron = "* */60 * * * *")
    @Scheduled(fixedDelay = 1000 * 60 * 25)
    @Retry(times = 5, on = org.hibernate.TransactionException.class)
    public Boolean updateArticleQueueScheduleIntegration() throws IOException {
        if (randomDelay(SchedulerServiceImpl.SCHEDULER_ARTICLE_QUEUE) < 0)
            return false;

        try {
            PageRequest page = PageRequest.of(0, 20);
            Page<ArticleQueueDTO> articleQueues = checkArticleQueue(page);

            while (articleQueues.hasContent()) {
                try {
                    articleQueues.forEach(articleQueue -> {
                        articleQueue.setStatus(QueueStatus.RUN);
                        articleQueue.setPublishUser("scheduler");

                        articleQueueService.save(articleQueue);

                        Optional<ArticleDTO> articleOpt = articleService.findOne(articleQueue.getArticleId());
                        if (articleOpt.isPresent()) {
                            ArticleDTO articleDTO = articleOpt.get();
                            articleDTO.setPublishDate(Instant.now());
                            articleDTO.setPublishUser("scheduler");
                            articleDTO.setStatus(ArticleStatus.PUBLISHED);

                            articleService.save(articleDTO);
                        }
                    });

                    if (articleQueues.hasNext()) {
                        articleQueues = checkArticleQueue(articleQueues.nextPageable());
                    } else {
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    @Transactional
//    @Scheduled(fixedRate = 1000 * 60 * 45)
//    @Scheduled(cron = "* */30 * * * *")
    @Scheduled(fixedDelay = 1000 * 60 * 20)
    @Retry(times = 5, on = org.hibernate.TransactionException.class)
    public Boolean updateHeadlineStartDateScheduleIntegration() throws IOException {
        if (randomDelay(SchedulerServiceImpl.SCHEDULER_HEADLINE_STARTDATE) < 0)
            return false;

        try {
            PageRequest page = PageRequest.of(0, 20);
            Page<HeadlineDTO> headlines = checkHeadlineSettingStartDateSchedule(page);

            while (headlines.hasContent()) {
                try {
                    headlines.forEach(headline -> {
                        HeadlineDTO headlineDTO = headline;
                        headlineDTO = headlineService.save(headlineDTO);
                        Optional<ArticleDTO> articleOpt = articleService.findOne(headlineDTO.getArticleId());

                        if (articleOpt.isPresent()) {
                            ArticleDTO articleDTO = articleOpt.get();
                            articleDTO.setHeadline(true);
                            articleService.save(articleDTO);
                        }
                    });

                    if (headlines.hasNext()) {
                        headlines = checkHeadlineSettingStartDateSchedule(headlines.nextPageable());
                    } else {
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    @Transactional
//    @Scheduled(cron = "0/10 0 * * * *")
//    @Scheduled(cron = "* */50 * * * *")
    @Scheduled(fixedDelay = 1000 * 60 * 20)
    @Retry(times = 5, on = org.hibernate.TransactionException.class)
    public Boolean updateHeadlineEndDateScheduleIntegration() throws IOException {
        if (randomDelay(SchedulerServiceImpl.SCHEDULER_HEADLINE_ENDDATE) < 0)
            return false;

        try {
            PageRequest page = PageRequest.of(0, 20);
            Page<HeadlineDTO> headlines = checkHeadlineSettingEndDateSchedule(page);

            while (headlines.hasContent()) {
                try {
                    headlines.forEach(headline -> {
                        HeadlineDTO headlineDTO = headline;
                        headlineDTO = headlineService.save(headlineDTO);

                        Optional<ArticleDTO> articleOpt = articleService.findOne(headlineDTO.getArticleId());

                        if (articleOpt.isPresent()) {
                            ArticleDTO articleDTO = articleOpt.get();
                            articleDTO.setHeadline(false);
                            articleService.save(articleDTO);
                        }
                    });

                    if (headlines.hasNext()) {
                        headlines = checkHeadlineSettingEndDateSchedule(headlines.nextPageable());
                    } else {
                        break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private int randomDelay(String hazelCastFlag) {
        int randomNumber = 1;
        try {
            randomNumber = ThreadLocalRandom.current().nextInt(1, 20);
            Thread.sleep((randomNumber * 10000));
            IMap<String, String> map = hazelcast.<String, String>getMap(hazelCastFlag);
            if (map.get(hazelCastFlag) != null) {
                return -1;
            }
            map.put(hazelCastFlag, "ON", 3600, TimeUnit.SECONDS); // data expired in 1 hour and will be evicted
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return randomNumber;
    }

    public Map.Entry<Integer, Date> getMaxMap(Map<Integer, Date> hm) {
        Map.Entry<Integer, Date> maxEntry = null;

        for (Map.Entry<Integer, Date> entry : hm.entrySet())
        {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
            {
                maxEntry = entry;
            }
        }

        return maxEntry;
    }

}
