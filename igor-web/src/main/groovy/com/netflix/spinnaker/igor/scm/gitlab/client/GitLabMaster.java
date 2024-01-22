/*
 * Copyright 2017 bol.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.spinnaker.igor.scm.gitlab.client;

import com.netflix.spinnaker.igor.scm.AbstractScmMaster;
import com.netflix.spinnaker.igor.scm.gitlab.client.model.GetFileContentResponse;
import com.netflix.spinnaker.igor.scm.gitlab.client.model.ListDirectoryResponse;
import com.netflix.spinnaker.kork.retrofit.exceptions.SpinnakerNetworkException;
import com.netflix.spinnaker.kork.retrofit.exceptions.SpinnakerServerException;
import com.netflix.spinnaker.kork.web.exceptions.NotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

/** Wrapper class for a collection of GitLab clients */
public class GitLabMaster extends AbstractScmMaster {

  private static final String FILE_CONTENT_TYPE = "file";

  GitLabClient gitLabClient;
  private final String baseUrl;

  public GitLabMaster(GitLabClient gitLabClient, String baseUrl) {
    this.gitLabClient = gitLabClient;
    this.baseUrl = baseUrl;
  }

  public GitLabClient getGitLabClient() {
    return gitLabClient;
  }

  public String getBaseUrl() {
    return baseUrl;
  }

  @Override
  public List<String> listDirectory(
      String projectKey, String repositorySlug, String path, String ref) {
    try {
      List<ListDirectoryResponse> response =
          gitLabClient.listDirectory(projectKey, repositorySlug, path, ref);
      return response.stream().map(r -> r.getPath()).collect(Collectors.toList());
    } catch (SpinnakerNetworkException e) {
      throw new NotFoundException("Could not find the server " + baseUrl);
    } catch (SpinnakerServerException e) {
      throw e;
    }
  }

  @Override
  public String getTextFileContents(
      String projectKey, String repositorySlug, String path, String ref) {
    try {
      GetFileContentResponse response =
          gitLabClient.getFileContent(projectKey, repositorySlug, path, ref);
      return new String(Base64.getDecoder().decode(response.content), StandardCharsets.UTF_8);
    } catch (SpinnakerNetworkException e) {
      throw new NotFoundException("Could not find the server " + baseUrl);
    } catch (SpinnakerServerException e) {
      throw e;
    }
  }

  @Override
  public Object getCommitDetails(String projectKey, String repositorySlug, String sha) {
    return gitLabClient.commitInfo(projectKey, repositorySlug, sha);
  }
}
