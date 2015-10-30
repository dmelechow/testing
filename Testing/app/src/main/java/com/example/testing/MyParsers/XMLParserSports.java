package com.example.testing.MyParsers;

import com.example.testing.MyEntry.MySportEntry;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Дмитрий on 20.09.2015.
 */
public class XMLParserSports {

    private static final String ns = null;

    public List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "sports");
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("sport")) {
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }


    public MySportEntry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "sport");
        int id = 0;
        String name = null;
        String teamFeed = null;
        int composerParam = 0;

        id = Integer.parseInt(parser.getAttributeValue(null, "id"));
        name = parser.getAttributeValue(null, "name");
        teamFeed = parser.getAttributeValue(null, "teamFeed");
        composerParam = Integer.parseInt(parser.getAttributeValue(null, "composerParam"));

        return new MySportEntry(id, name, teamFeed, composerParam);
    }


    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
