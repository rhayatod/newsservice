package com.investasikita.news.service;

import java.io.IOException;

public interface SchedulerService {

    public Boolean updateArticleScheduleIntegration() throws IOException;
    public Boolean updateArticleQueueScheduleIntegration() throws IOException;
    public Boolean updateHeadlineStartDateScheduleIntegration() throws IOException;
    public Boolean updateHeadlineEndDateScheduleIntegration() throws IOException;
}
