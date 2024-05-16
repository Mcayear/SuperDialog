function main()
    local playerName = player:getName()  -- 调用Java方法获取玩家名称
    local entityName = entity:getName()  -- 调用Java方法获取实体名称
    local title = "Welcome " .. playerName .. "!"
    local description = "You are interacting with " .. entityName .. ". Please choose an option:"
    local options = {"Option 1", "Option 2"}
    return {title, description, options, { "oneDialog", "twoDialog" }}
end

function oneDialog()
    local playerName = player:getName()
    local entityName = entity:getName()
    local title = "You chose Option 1, " .. playerName .. "!"
    local description = "You are still interacting with " .. entityName .. ". Choose again:"
    local options = {"Close", "Option 2"}
    return {title, description, options, { "close", "twoDialog" }}
end

function twoDialog()
    local playerName = player:getName()
    local entityName = entity:getName()
    local title = "You chose Option 2, " .. playerName .. "!"
    local description = "You are still interacting with " .. entityName .. ". Choose again:"
    local options = {"Option 1", "Close"}
    return {title, description, options, { "oneDialog", "close" }}
end

function close()
    -- 此函数没有返回值
end
