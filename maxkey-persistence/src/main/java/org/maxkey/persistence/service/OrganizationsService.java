/*
 * Copyright [2020] [MaxKey of copyright http://www.maxkey.top]
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
 

package org.maxkey.persistence.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.mybatis.jpa.persistence.JpaBaseService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.maxkey.domain.Organizations;
import org.maxkey.persistence.kafka.KafkaIdentityAction;
import org.maxkey.persistence.kafka.KafkaIdentityTopic;
import org.maxkey.persistence.kafka.KafkaPersistService;
import org.maxkey.persistence.mapper.OrganizationsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;

@Service
public class OrganizationsService  extends JpaBaseService<Organizations>{

    @Autowired
    KafkaPersistService kafkaPersistService;
    
	public OrganizationsService() {
		super(OrganizationsMapper.class);
	}

	/* (non-Javadoc)
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public OrganizationsMapper getMapper() {
		// TODO Auto-generated method stub
		return (OrganizationsMapper)super.getMapper();
	}
	
	 public boolean insert(Organizations organization) {
	     if(super.insert(organization)){
	    	 kafkaPersistService.send(
                     KafkaIdentityTopic.ORG_TOPIC, organization, KafkaIdentityAction.CREATE_ACTION);
             return true;
         }
         return false;
	 }
	 
	 public boolean update(Organizations organization) {
	     if(super.update(organization)){
	    	 kafkaPersistService.send(
                     KafkaIdentityTopic.ORG_TOPIC, organization, KafkaIdentityAction.UPDATE_ACTION);
             return true;
         }
         return false;
     }
 
	 public boolean delete(Organizations organization) {
	     if(super.delete(organization)){
	    	 kafkaPersistService.send(
                     KafkaIdentityTopic.ORG_TOPIC, organization, KafkaIdentityAction.DELETE_ACTION);
             return true;
         }
         return false;
	 }
	 
	    public boolean importing(MultipartFile file) {
	        if(file ==null){
	            return false;
	        }
	        InputStream is = null;
	        Workbook wb = null;
	        List<Organizations> orgsList = null;
	        try {
	            is = file.getInputStream();
	            
	            String xls = ".xls";
	            String xlsx = ".xlsx";
	            int columnSize = 46;
	            orgsList = Lists.newArrayList();
	            if (file.getOriginalFilename().toLowerCase().endsWith(xls)) {
	                wb = new HSSFWorkbook(is);
	            } else if (file.getOriginalFilename().toLowerCase().endsWith(xlsx)) {
	                wb = new XSSFWorkbook(is);
	            } else {
	                throw new RuntimeException("maxKey??????????????????Excel??????");
	            }

	            int sheetSize = wb.getNumberOfSheets();
	            //??????sheet???
	            for (int i = 0; i < sheetSize; i++) {
	                Sheet sheet = wb.getSheetAt(i);

	                int rowSize = sheet.getLastRowNum() + 1;
	                //?????????
	                for (int j = 1; j < rowSize; j++) {
	                    Row row = sheet.getRow(j);
	                    //??????????????????3???
	                    if (row == null || j <3 ) {
	                        continue;
	                    } else {
	                        //?????????????????????
	                        Organizations organization =new Organizations();

	                        for (int k = 0; k < columnSize; k++) {
	                            if (k == 0) {
	                                // ????????????
	                                Cell cell = row.getCell(k);
	                                organization.setParentId(getValue(cell));
	                            } else if (k == 1) {
	                                // ????????????
	                                Cell cell = row.getCell(k);
	                                organization.setParentName(getValue(cell));
	                            } else if (k == 2) {
	                                // ????????????
	                                Cell cell = row.getCell(k);
	                                organization.setId(getValue(cell));
	                            } else if (k == 3) {
	                                // ????????????
	                                Cell cell = row.getCell(k);
	                                organization.setName(getValue(cell));
	                            } else if (k == 4) {
	                                // ????????????
	                                Cell cell = row.getCell(k);
	                                organization.setFullName(getValue(cell));
	                            } else if (k == 5) {
	                                // ????????????
	                                Cell cell = row.getCell(k);
	                                organization.setCodePath(getValue(cell));
	                            } else if (k == 6) {
	                                // ????????????
	                                Cell cell = row.getCell(k);
	                                organization.setNamePath(getValue(cell));
	                            } else if (k == 7) {
	                                // ????????????
	                                Cell cell = row.getCell(k);
	                                organization.setType(getValue(cell));
	                            } else if (k == 8) {
	                                // ??????????????????
	                                Cell cell = row.getCell(k);
	                                organization.setDivision(getValue(cell));
	                            } else if (k == 9) {
	                                // ??????
	                                Cell cell = row.getCell(k);
	                                String level=getValue(cell);
	                                organization.setLevel(level.equals("") ? "1" : level);
	                            } else if (k == 10) {
	                                // ??????
	                                Cell cell = row.getCell(k);
	                                String sortIndex=getValue(cell);
	                                organization.setSortIndex(sortIndex.equals("") ? "1" : sortIndex);
	                            } else if (k == 11) {
	                                // ?????????
	                                Cell cell = row.getCell(k);
	                                organization.setContact(getValue(cell));
	                            } else if (k == 12) {
	                                // ????????????
	                                Cell cell = row.getCell(k);
	                                organization.setPhone(getValue(cell));
	                            }else if (k == 13) {
	                                // ??????
	                                Cell cell = row.getCell(k);
	                                organization.setEmail(getValue(cell));
	                            }else if (k == 14) {
	                                // ??????
	                                Cell cell = row.getCell(k);
	                                organization.setFax(getValue(cell));
	                            }else if (k == 24) {
	                                // ??????-??????
	                                Cell cell = row.getCell(k);
	                                organization.setCountry(getValue(cell));
	                            }else if (k == 25) {
	                                // ??????-???
	                                Cell cell = row.getCell(k);
	                                organization.setRegion(getValue(cell));
	                            }else if (k == 26) {
	                                // ??????-??????
	                                Cell cell = row.getCell(k);
	                                organization.setLocality(getValue(cell));
	                            }else if (k == 27) {
	                                // ??????-??????
	                                Cell cell = row.getCell(k);
	                                organization.setLocality(getValue(cell));
	                            }else if (k == 28) {
	                                // ??????
	                                Cell cell = row.getCell(k);
	                                organization.setPostalCode(getValue(cell));
	                            }else if (k == 29) {
	                                // ????????????
	                                Cell cell = row.getCell(k);
	                                organization.setDescription(getValue(cell));
	                            }
	                        }
	                        organization.setStatus("1");
	                        orgsList.add(organization);
	                    }
	                    
	                }
	            }
	            // ????????????
	            if(CollectionUtils.isEmpty(orgsList)){
	                orgsList = orgsList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getId()))), ArrayList::new));
	            }
	           
	        } catch (IOException e) {
	            e.printStackTrace();
	        }finally {
	            if (is != null) {
	                try {
	                    is.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	            if(wb != null) {
	                try {
	                    wb.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	       
	        return batchInsert(orgsList);
	    }

	 /**
	     *       ??????????????????????????????
     *
     * @param cell
     * @return
     */
    public static String getValue(Cell cell) {
        if (cell == null) {
            return "";
        } else if (cell.getCellType() == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == CellType.NUMERIC) {
            cell.setBlank();
            return String.valueOf(cell.getStringCellValue().trim());
        } else {
            return String.valueOf(cell.getStringCellValue().trim());
        }
    }
}
