package net.elbadri.gitapp.service;

import net.elbadri.gitapp.model.GitRepo;
import net.elbadri.gitapp.model.GitUsersResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GitRepoServiceAPI {
    @GET("search/users")
    public Call<GitUsersResponse> searchUsers(@Query("q") String query);
    @GET("users/{u}/repos")
    public Call<List<GitRepo>> userRepositories(@Path("u") String login);
}
