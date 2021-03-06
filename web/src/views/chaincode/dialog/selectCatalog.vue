/*
 * Copyright 2014-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
<template>
    <div>
        <el-dialog :title="$t('title.selectDirectory')" :visible.sync="dialogVisible" :before-close="close" class="dialog-wrapper" width="433px" :center="true">
            <div>
                <el-form :model="folderFrom" :rules="rules" ref="folderFrom" label-width="100px" class="demo-ruleForm">
                    <el-form-item :label="$t('dialog.folderName')" prop="folderName" style="width:330px">
                        <el-select v-model="folderFrom.folderName" :placeholder="$t('placeholder.selected')" class="block-network">
                            <el-option v-for="item in options" :key="item.folderName" :label="item.folderName" :value="item.folderName">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-form>
            </div>
            <div slot="footer" class="dialog-footer">
                <el-button @click="close">{{$t('dialog.cancel')}}</el-button>
                <el-button type="primary" @click="submit('folderFrom')">{{$t('dialog.confirm')}}</el-button>
            </div>
        </el-dialog>
    </div>
</template>
<script>
import { getContractPathList } from "@/util/api"
export default {
    name: "selectCatalog",
    props: ['show'],
    computed: {
        rules() {
            var obj = {
                folderName: [
                    {
                        required: true,
                        message: `${this.$t('dialog.pleaseType')} ${this.$t('dialog.folderName')}`,
                        trigger: "blur"
                    }
                ]
            }
            return obj
        }
    },
    data: function () {
        return {
            options: [],
            folderFrom: {
                folderName: ""
            },
            dialogVisible: this.show,
            pathList: [],
            folderList: []
        }
    },
    mounted: function () {
        this.getContractPaths();
    },
    methods: {
        getContractPaths() {
            getContractPathList(localStorage.getItem("groupId")).then(res => {
                if (res.status == 200) {
                    this.pathList = res.data;
                    let num = 0;
                    this.folderList = []
                    for (let i = 0; i < this.pathList.length; i++) {
                        let item = {
                            folderName: this.pathList[i].contractPath,
                            folderId: new Date().getTime() + this.pathList[i].contractPath,
                            folderActive: false,
                            groupId: localStorage.getItem("groupId"),
                            modifyTime: this.pathList[i].modifyTime
                        };
                        this.folderList.push(item)
                        if (this.pathList[i].contractPath == this.userFolader) {
                            num++
                        }
                    }
                    this.changeOptions();
                } else {
                    this.$message({
                        type: "error",
                        message: this.$chooseLang(res.data.code)
                    });
                }
            })
                .catch(err => {
                    this.$message({
                        type: "error",
                        message: this.$t('text.systemError')
                    });
                });
        },
        changeOptions: function () {
            this.options = []
            if (this.folderList.length) {
                let num = 0;
                for (let i = 0; i < this.folderList.length; i++) {
                    if (this.folderList[i].folderName == "/") {
                        num++
                    }
                }
                if (num == 0) {
                    let data = {
                        folderName: "/",
                        folderId: 1,
                    }
                    this.folderList.unshift(data)
                }
                this.options = this.folderList
            } else {
                this.options = [{
                    folderName: "/",
                    folderId: 1,
                }];
            }
            this.folderFrom.folderName = this.options[0].folderName
        },
        submit: function (formName) {
            this.$refs[formName].validate(valid => {
                if (valid) {
                    this.$emit("success", this.folderFrom.folderName)
                } else {
                    return false
                }
            })
        },
        close: function () {
            this.$emit("close")
        }
    }
}
</script>

<style scoped>
.demo-ruleForm >>> .el-form-item__label {
    padding: 0 8px 0 0;
}
</style>    