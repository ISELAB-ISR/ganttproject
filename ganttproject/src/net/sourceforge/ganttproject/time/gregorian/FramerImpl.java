/*
 LICENSE:
 
 This program is free software; you can redistribute it and/or modify  
 it under the terms of the GNU General Public License as published by  
 the Free Software Foundation; either version 2 of the License, or     
 (at your option) any later version.                                   
 
 Copyright (C) 2004, GanttProject Development Team
 */
package net.sourceforge.ganttproject.time.gregorian;

import java.util.Calendar;
import java.util.Date;
import net.sourceforge.ganttproject.calendar.CalendarFactory;
import net.sourceforge.ganttproject.time.DateFrameable;

/**
 * Created by IntelliJ IDEA.
 * 
 * @author bard
 */
public class FramerImpl implements DateFrameable {
    private final int myCalendarField;

    public FramerImpl(int calendarField) {
        myCalendarField = calendarField;
    }

    public Date adjustRight(Date baseDate) {
        Calendar c = CalendarFactory.newCalendar();
        c.setTime(baseDate);
        clearFields(c);
        c.add(myCalendarField, 1);
        return c.getTime();
    }

    private void clearFields(Calendar c) {
        for (int i = myCalendarField + 1; i <= Calendar.MILLISECOND; i++) {
            c.clear(i);
        }
    }

    public Date adjustLeft(Date baseDate) {
        Calendar c = CalendarFactory.newCalendar();
        c.setTime(baseDate);
        clearFields(c);
        // Date beforeClear = c.getTime();
        // if (beforeClear.compareTo(c.getTime())==0) {
        //     c.add(Calendar.MILLISECOND, -1);
        // }
        return c.getTime();
    }

    public Date jumpLeft(Date baseDate) {
        Calendar c = CalendarFactory.newCalendar();
        c.setTime(baseDate);
        c.add(myCalendarField, -1);
        return c.getTime();
    }
}