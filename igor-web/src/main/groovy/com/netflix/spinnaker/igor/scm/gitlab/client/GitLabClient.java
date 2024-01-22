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

import com.netflix.spinnaker.igor.scm.gitlab.client.model.Commit;
import com.netflix.spinnaker.igor.scm.gitlab.client.model.CompareCommitsResponse;
import com.netflix.spinnaker.igor.scm.gitlab.client.model.GetFileContentResponse;
import com.netflix.spinnaker.igor.scm.gitlab.client.model.ListDirectoryResponse;
import java.util.List;
import java.util.Map;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;

/** Interface for interacting with a GitLab REST API https://docs.gitlab.com/ce/api/README.html */
public interface GitLabClient {

  /** https://docs.gitlab.com/ce/api/repositories.html#compare-branches-tags-or-commits */
  @GET("/api/v4/projects/{projectKey}%2F{repositorySlug}/repository/compare")
  CompareCommitsResponse getCompareCommits(
      @Path("projectKey") String projectKey,
      @Path("repositorySlug") String repositorySlug,
      @QueryMap Map queryMap);

  /** https://docs.gitlab.com/ee/api/repositories.html#list-repository-tree */
  @GET("/api/v4/projects/{projectKey}%2F{repositorySlug}/repository/tree")
  List<ListDirectoryResponse> listDirectory(
      @Path("projectKey") String projectKey,
      @Path("repositorySlug") String repositorySlug,
      @Query("path") String path,
      @Query("ref") String ref);

  @GET("/api/v4/projects/{projectKey}%2F{repositorySlug}/repository/files/{path}")
  GetFileContentResponse getFileContent(
      @Path("projectKey") String projectKey,
      @Path("repositorySlug") String repositorySlug,
      @Path(value = "path", encode = false) String path,
      @Query("ref") String ref);

  /** https://docs.gitlab.com/ee/api/commits.html#get-a-single-commit */
  @GET("/api/v4/projects/{projectKey}%2F{repositorySlug}/repository/commits/{sha}")
  Commit commitInfo(
      @Path("projectKey") String projectKey,
      @Path("repositorySlug") String repositorySlug,
      @Path("sha") String sha);
}
