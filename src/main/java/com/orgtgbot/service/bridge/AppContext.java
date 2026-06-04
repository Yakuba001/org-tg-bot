package com.orgtgbot.service.bridge;

import com.orgtgbot.service.services.DateService;
import com.orgtgbot.service.services.GeneralService;
import com.orgtgbot.service.services.ProbegService;

public record AppContext(GeneralService generalService, ProbegService probegService, DateService dateService) { }
