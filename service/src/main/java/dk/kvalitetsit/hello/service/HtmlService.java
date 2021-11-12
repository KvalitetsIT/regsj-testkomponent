package dk.kvalitetsit.hello.service;

import dk.kvalitetsit.hello.service.model.HtmlInfo;

import java.net.UnknownHostException;

public interface HtmlService {
    HtmlInfo getHtmlInfo() throws UnknownHostException;
}
