package com.octopus.services.impl;

import com.atlassian.bamboo.Key;
import com.atlassian.bamboo.ResultKey;
import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.chains.ChainStorageTag;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.configuration.ConfigurationMapImpl;
import com.atlassian.bamboo.credentials.CredentialsData;
import com.atlassian.bamboo.plan.PlanKey;
import com.atlassian.bamboo.repository.RepositoryDefinition;
import com.atlassian.bamboo.serialization.WhitelistedSerializable;
import com.atlassian.bamboo.task.RuntimeTaskContext;
import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.bamboo.task.runtime.RuntimeTaskDefinition;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import com.atlassian.bamboo.v2.build.BuildContext;
import com.atlassian.bamboo.v2.build.BuildKey;
import com.atlassian.bamboo.v2.build.CommonContext;
import com.atlassian.bamboo.v2.build.CurrentResult;
import com.atlassian.bamboo.v2.build.trigger.TriggerReason;
import com.atlassian.bamboo.variable.VariableContext;
import com.octopus.constants.OctoConstants;
import com.octopus.constants.OctoTestConstants;
import com.octopus.services.MockObjectService;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of the MockObjectService
 */
@SuppressWarnings("ConstantConditions")
public class MockObjectServiceImpl implements MockObjectService {
    public static final Logger LOGGER = LoggerFactory.getLogger(MockObjectServiceImpl.class);

    public String getApiKey() {
        return StringUtils.defaultIfBlank(
                System.getProperty(OctoTestConstants.API_KEY_SYSTEM_PROP),
                OctoTestConstants.DUMMY_API_KEY);
    }

    public TaskContext getTaskContext() {
        return getTaskContext(new File("."), false, "**/first-bamboo-int.0.0.1.zip");
    }

    public TaskContext getTaskContext(@NotNull final File workingDir) {
        return getTaskContext(workingDir, false, "**/first-bamboo-int.0.0.1.zip");
    }

    public TaskContext getTaskContext(@NotNull final File workingDir, final boolean forceUpload) {
        return getTaskContext(workingDir, forceUpload, "**/first-bamboo-int.0.0.1.zip");
    }

    public TaskContext getTaskContext(@NotNull final File workingDir,
                                      final boolean forceUpload,
                                      @NotNull final String pattern) {
        return getTaskContext(workingDir, forceUpload, pattern, getApiKey());
    }

    public TaskContext getTaskContext(@NotNull final File workingDir,
                                      final boolean forceUpload,
                                      @NotNull final String pattern,
                                      @NotNull final String apiKey) {
        return getTaskContext(workingDir, forceUpload, pattern, apiKey, OctoConstants.LOCAL_OCTOPUS_INSTANCE);
    }

    public TaskContext getTaskContext(@NotNull final File workingDir,
                                      final boolean forceUpload,
                                      @NotNull final String pattern,
                                      @NotNull final String apiKey,
                                      @NotNull final String octopusUrl) {
        return getTaskContext(workingDir, forceUpload, pattern, apiKey, OctoConstants.LOCAL_OCTOPUS_INSTANCE, "0.0.1");
    }

    public TaskContext getTaskContext(@NotNull final File workingDir,
                                      final boolean forceUpload,
                                      @NotNull final String pattern,
                                      @NotNull final String apiKey,
                                      @NotNull final String octopusUrl,
                                      @NotNull final String releaseVersion) {
        checkNotNull(workingDir);
        checkNotNull(pattern);
        checkNotNull(apiKey);

        final MockObjectService me = this;

        return new TaskContext() {
            private RecordingBuildLogger logger = new RecordingBuildLogger();

            public long getId() {
                return 0;
            }

            @org.jetbrains.annotations.NotNull
            public String getPluginKey() {
                return "PluginKey";
            }

            @Nullable
            public String getUserDescription() {
                return null;
            }

            public boolean isEnabled() {
                return true;
            }

            public boolean isFinalising() {
                return false;
            }

            @org.jetbrains.annotations.NotNull
            public BuildContext getBuildContext() {
                return null;
            }

            @org.jetbrains.annotations.NotNull
            public CommonContext getCommonContext() {
                return me.getCommonContext();
            }

            @org.jetbrains.annotations.NotNull
            public BuildLogger getBuildLogger() {
                return logger;
            }

            @org.jetbrains.annotations.NotNull
            public File getRootDirectory() {
                return workingDir;
            }

            @org.jetbrains.annotations.NotNull
            public File getWorkingDirectory() {
                return workingDir;
            }

            @org.jetbrains.annotations.NotNull
            public ConfigurationMap getConfigurationMap() {
                final ConfigurationMap retValue = new ConfigurationMapImpl();
                retValue.put(OctoConstants.SERVER_URL, octopusUrl);
                retValue.put(OctoConstants.API_KEY, apiKey);
                retValue.put(OctoConstants.FORCE, forceUpload + "");
                retValue.put(OctoConstants.PUSH_PATTERN, pattern);
                retValue.put(OctoConstants.PROJECT_NAME, OctoTestConstants.TEST_PROJECT);
                retValue.put(OctoConstants.RELEASE_VERSION, releaseVersion);
                return retValue;
            }

            @Nullable
            public Map<String, String> getRuntimeTaskContext() {
                return null;
            }

            @Nullable
            public Map<String, WhitelistedSerializable> getRuntimeTaskData() {
                return null;
            }

            public boolean doesTaskProduceTestResults() {
                return false;
            }
        };
    }

    public CommonContext getCommonContext() {
        return new CommonContext() {

            @NotNull
            public BuildKey getBuildKey() {
                return null;
            }

            public long getEntityId() {
                return 0;
            }

            public Key getEntityKey() {
                return null;
            }

            public ResultKey getResultKey() {
                return new ResultKey() {

                    @NotNull
                    public String getKey() {
                        return "key";
                    }

                    @NotNull
                    public Key getEntityKey() {
                        return new Key() {

                            @NotNull
                            public String getKey() {
                                return "entity key";
                            }
                        };
                    }

                    public int getResultNumber() {
                        return 0;
                    }
                };
            }

            public String getDisplayName() {
                return null;
            }

            public CurrentResult getCurrentResult() {
                return null;
            }

            @NotNull
            public ErrorCollection getErrorCollection() {
                return null;
            }

            @NotNull
            public List<TaskDefinition> getTaskDefinitions() {
                return null;
            }

            @NotNull
            public List<RuntimeTaskDefinition> getRuntimeTaskDefinitions() {
                return null;
            }

            @NotNull
            public RuntimeTaskContext getRuntimeTaskContext() {
                return null;
            }

            @NotNull
            public VariableContext getVariableContext() {
                return null;
            }

            @NotNull
            public TriggerReason getTriggerReason() {
                return null;
            }

            @NotNull
            public Map<Long, RepositoryDefinition> getRepositoryDefinitionMap() {
                return null;
            }

            @NotNull
            public List<RepositoryDefinition> getRepositoryDefinitions() {
                return null;
            }

            @NotNull
            public Iterable<CredentialsData> getSharedCredentials() {
                return null;
            }

            @NotNull
            public Map<PlanKey, ChainStorageTag> getChainStorageTags() {
                return null;
            }
        };
    }
}
