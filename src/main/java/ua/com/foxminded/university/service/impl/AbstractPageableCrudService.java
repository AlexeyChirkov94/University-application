package ua.com.foxminded.university.service.impl;

import org.apache.log4j.Logger;
import ua.com.foxminded.university.service.interfaces.CrudService;

public abstract class AbstractPageableCrudService<REQUEST, RESPONSE> implements CrudService<REQUEST, RESPONSE> {

    private static final Logger LOGGER = Logger.getLogger(AbstractPageableCrudService.class);

    protected static final int ITEMS_PER_PAGE = 5;

    protected int parsePageNumber(String page, long itemsCount, int defaultValue){

        long maxPage = itemsCount / ITEMS_PER_PAGE + ((itemsCount % ITEMS_PER_PAGE == 0) ? 0 : 1);

        if (page == null){
            return defaultValue;
        }
        try {
            final int integerPage = Integer.parseInt(page);
            if (integerPage > maxPage) {
                return (int)maxPage;
            }
            if (integerPage <= 0) {
                return defaultValue;
            }
            return integerPage;
        } catch (NumberFormatException e) {
            LOGGER.warn("can`t lead sting page to integer");
            return defaultValue;
        }
    }

}
