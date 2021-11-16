package dk.kvalitetsit.regsj.testkomponent.service;

import dk.kvalitetsit.regsj.testkomponent.service.model.HtmlInfo;

import java.net.UnknownHostException;

public interface HtmlService {
    HtmlInfo getHtmlInfo() throws UnknownHostException;
}
